## 流式查询记录
可知只是prepareStatement时候改变了参数为ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY，并且设置了PreparedStatement的fetchsize为Integer.MIN_VALUE。
```java
public void selectData(String sqlCmd,) throws SQLException {

    validate(sqlCmd);

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {

        conn = petadataSource.getConnection();
        
        stmt = conn.prepareStatement(sqlCmd, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            
        rs = stmt.executeQuery();

        try {
            while(rs.next()){
                try {
                    System.out.println("one:" + rs.getString(1) + "two:" + rs.getString(2) + "thrid:" + rs.getString(3));
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        } finally {
            close(stmt, rs, conn);

        }
}
```
MyBatis 流式查询的核心接口是 Cursor
可以看到 Cursor 继承了 Iterable接口。由Iterable知Cursor 是可关闭和遍历的。其中Cursor 的核心方法

```java
//于在取数据之前判断 Cursor 对象是否是打开状态。只有当打开时 Cursor 才能取数据
    boolean isOpen();
    //用于判断查询结果是否全部取完
    boolean isConsumed();
    //返回已经获取了多少条数据
    int getCurrentIndex();

```
使用流式查询不能简单的直接执行Mapper，因为传统执行Mapper方法的结果是Mapper方法执行完连接就关闭了，导致异常，所以需要我们自己手动处理

自己通过 SqlSession 获取Mapper类

mapper.xml

```
<select id="testBatch" resultType="com.sinoxk.order.model.OrderDetail">
        SELECT *
        from
        order_detail
    </select>

```
接口
mapper.class
```
Cursor<OrderDetail> testBatch();
```

这里需要注意返回的结果是Cursor而不是集合
```java
	@Autowired
    @Qualifier("clickHouseSqlSessionFactoryBean")
    SqlSessionFactory sqlSessionFactory;

	@Test
    public void testSelectBatch() throws Exception {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            Cursor<OrderDetail> orderDetails = sqlSession.getMapper(CKOrderDetailMapper.class).testBatch();
            for (OrderDetail orderDetail : orderDetails) {
                System.out.println(orderDetail.getDetailId());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

```
这里我因为使用了多数据源所以需要使用@Qualifier指定注入的SqlSession，单数据源可以忽略

这里我测试的数据量是3w+，我将JVM内存调到64m运行
JVM参数
```
-Xms64m
-Xmx64m
```
打开 Java VisualVM对JVM进行监控
发现即使3w+的数据也没有超出内存

而使用普通的查询直接报错OOM
方法二
用 TransactionTemplate 来执行一个数据库事务，保证连接一直是打开的。
```java
@Resource
    private PlatformTransactionManager transactionManager;

    @Test
    public void test23232() {
        TransactionTemplate transactionTemplate =
                new TransactionTemplate(transactionManager);  // 1
        transactionTemplate.execute(status -> {               // 2
            try (Cursor<OrderDetail> orderDetails = ckOrderDetailMapper.testBatch()) {
                orderDetails.forEach(orderDetail -> { });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
```

方法三
也是和方法二类似,方法二是编程式事务，方法三是声明式事务，这里需要注意事务的失效问题
```java
@Transactional
    public void scanFoo3(@PathVariable("limit") int limit) throws Exception {
        try (Cursor<OrderDetail> orderDetails = ckOrderDetailMapper.testBatch()) {
            orderDetails.forEach(foo -> { });
        }
    }

```

## MyBatis 流式查询接口

因为 Cursor 实现了迭代器接口，因此在实际使用当中，从 Cursor 取数据非常简单：
```java
try(Cursor cursor = mapper.querySomeData()) {
    cursor.forEach(rowObject -> {
        // ...
    });
}

```
使用 try-resource 方式可以令 Cursor 自动关闭。

但构建 Cursor 的过程不简单
我们举个实际例子。下面是一个 Mapper 类：
```java
@Mapper
public interface FooMapper {
    @Select("select * from foo limit #{limit}")
    Cursor<Foo> scan(@Param("limit") int limit);
}
```
方法 scan() 是一个非常简单的查询。我们在定义这个方时，指定返回值为 Cursor 类型，MyBatis 就明白这个查询方法是一个流式查询。

然后我们再写一个 SpringMVC Controller 方法来调用 Mapper（无关的代码已经省略）：
```java
@GetMapping("foo/scan/0/{limit}")
public void scanFoo0(@PathVariable("limit") int limit) throws Exception {
    try (Cursor<Foo> cursor = fooMapper.scan(limit)) {  // 1
        cursor.forEach(foo -> {});                      // 2
    }
}

```
假设 fooMapper 是 @Autowired 进来的。注释 1 处是获取 Cursor 对象并保证它能最后关闭；2 处则是从 cursor 中取数据。

上面的代码看上去没什么问题，但是执行 scanFoo0(int) 时会报错：

java.lang.IllegalStateException: A Cursor is already closed.这是因为我们前面说了在取数据的过程中需要保持数据库连接，而 Mapper 方法通常在执行完后连接就关闭了，因此 Cusor 也一并关闭了。

所以，解决这个问题的思路不复杂，保持数据库连接打开即可。我们至少有三种方案可选。

方案一：SqlSessionFactory

我们可以用 SqlSessionFactory 来手工打开数据库连接，将 Controller 方法修改如下：



```
@GetMapping("foo/scan/1/{limit}")
public void scanFoo1(@PathVariable("limit") int limit) throws Exception {
    try (
        SqlSession sqlSession = sqlSessionFactory.openSession();  // 1
        Cursor<Foo> cursor = 
              sqlSession.getMapper(FooMapper.class).scan(limit)   // 2
    ) {
        cursor.forEach(foo -> { });
    }
}
```

上面的代码中，1 处我们开启了一个 SqlSession （实际上也代表了一个数据库连接），并保证它最后能关闭；2 处我们使用 SqlSession 来获得 Mapper 对象。这样才能保证得到的 Cursor 对象是打开状态的。

方案二：TransactionTemplate

在 Spring 中，我们可以用 TransactionTemplate 来执行一个数据库事务，这个过程中数据库连接同样是打开的。代码如下：


```
@GetMapping("foo/scan/2/{limit}")
public void scanFoo2(@PathVariable("limit") int limit) throws Exception {
    TransactionTemplate transactionTemplate = 
            new TransactionTemplate(transactionManager);  // 1

    transactionTemplate.execute(status -> {               // 2
        try (Cursor<Foo> cursor = fooMapper.scan(limit)) {
            cursor.forEach(foo -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    });
}
```
上面的代码中，1 处我们创建了一个 TransactionTemplate 对象（此处 transactionManager 是怎么来的不用多解释，本文假设读者对 Spring 数据库事务的使用比较熟悉了），2 处执行数据库事务，而数据库事务的内容则是调用 Mapper 对象的流式查询。注意这里的 Mapper 对象无需通过 SqlSession 创建。

方案三：@Transactional 注解
这个本质上和方案二一样，代码如下：
```
@GetMapping("foo/scan/3/{limit}")
@Transactional
public void scanFoo3(@PathVariable("limit") int limit) throws Exception {
    try (Cursor<Foo> cursor = fooMapper.scan(limit)) {
        cursor.forEach(foo -> { });
    }
}
```
它仅仅是在原来方法上面加了个 @Transactional 注解。这个方案看上去最简洁，但请注意 Spring 框架当中注解使用的坑：只在外部调用时生效。在当前类中调用这个方法，依旧会报错。