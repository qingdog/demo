# 学习多线程使用Callable接口
```java
package com.wm.file.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.wm.file.entity.UserEntity;
import com.wm.file.mapper.UserMapper;
import com.wm.file.service.UserService;
import com.wm.file.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    /**
     * 每批次处理的数据量
     */
    private static final int LIMIT = 40000;
    @Autowired
    private UserMapper userMapper;

    @Resource(name = "taskExecutor")
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public List<UserEntity> selectAll(Integer total,Integer limit) {
        long start = System.currentTimeMillis();
        List<MyCallableTsk> myCallableTsks = new ArrayList<>();
        // 计算出多少页，即循环次数
        int count = total / limit + (total % limit > 0 ? 1 : 0);
        System.out.println("本次任务量: "+count);

        // CountDownLatch闭锁用于计数（count为任务量）
        CountDownLatch cd = new CountDownLatch(count);
        for (int i = 1; i <= count; i++) {
            // 创建 Callable可调用的实现类 的集合 用于多线程执行
            myCallableTsks.add(new MyCallableTsk(i, limit, cd));
        }
        List<UserEntity> userEntityList = new ArrayList<>();
        try {
            // 线程池调用全部 Callable可调用的实现类执行call方法
            List<Future<List<UserEntity>>> futures = threadPoolExecutor.invokeAll(myCallableTsks);
            for (Future<List<UserEntity>> future : futures) {
                userEntityList.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("主线程：" + Thread.currentThread().getName() + " , 导出指定数据成功 , 共导出数据：" + userEntityList.size() + " ,查询数据任务执行完毕共消耗时 ：" + (end - start) + "ms");
        return userEntityList;
    }
    class MyCallableTsk implements Callable<List<UserEntity>>{
        private UserMapper userMapper;

        private CountDownLatch cd;

        private Integer pageNum;
        private Integer pageSize;

        public MyCallableTsk(Integer pageNum, Integer pageSize, CountDownLatch cd){
            this.pageNum =pageNum;
            this.pageSize =pageSize;
            this.cd=cd;
        }
        @Override
        public List<UserEntity> call() throws Exception {
            System.out.println("线程：" + Thread.currentThread().getName() + " , 开始读取数据------");
            long start = System.currentTimeMillis();
            userMapper = (UserMapper) SpringUtil.getBean("userMapper");

            PageHelper.startPage(pageNum,pageSize);
            // PageHelper开始分页后，再用mybatis查询全部数据
            List<UserEntity> userEntityList = UserServiceImpl.this.userMapper.selectAll();

            System.out.println("线程：" + Thread.currentThread().getName() + " , 读取数据  "+userEntityList.size()+",页数:"+pageNum+ "耗时 ：" + (System.currentTimeMillis() - start)+ "ms");
            cd.countDown();
            System.out.println("剩余任务数  ===========================> " + cd.getCount());
            return userEntityList;
        }
    }
}
```



# 多线程实现百万级数据导出到excel

## **考虑前提：**

大数据量导出到文件，首先需要考虑的是内存溢出的场景：数据库读取数据到内存中、将数据写入到excel进行大量的IO操作。然后考虑到一个文件数据过大，用户打开慢，体验不好。针对这些问题的考虑，采用多线程的方式多个线程同时处理查询数据，一个线程生成一个excel，最后在合并数据返回，以达到提高效率的目的。

## **实现思路：**
1.使用easyExecl结合批量线程池执行,结合线程分批处理数据缩短查询数据库时间和导出数据时间,查询数据速度比较理想
测试接口:http://localhost:8080/exportEasyExcel?pageNum=0&pageSize=100000&limit=4000
2.使用easyExecl结合循环线程池执行,结合线程分批处理数据缩短查询数据库时间和导出数据时间,查询数据速度不理想
测试接口:http://localhost:8080/exportEasyExcelV1?pageNum=0&pageSize=60000&limit=2000
3.使用SXSSFWorkbook结合批量线程池执行,结合线程分批处理数据缩短查询数据库时间和导出数据时间,效果最优最理想
http://localhost:8080/exportEasyExcelV4?pageNum=0&total=1000&limit=100

模拟sql可以结合测试类本地造数据:
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `ceate_by` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `test1` varchar(255) DEFAULT NULL,
  `test2` varchar(255) DEFAULT NULL,
  `test3` varchar(255) DEFAULT NULL,
  `test4` varchar(255) DEFAULT NULL,
  `test5` varchar(255) DEFAULT NULL,
  `test6` varchar(255) DEFAULT NULL,
  `test7` varchar(255) DEFAULT NULL,
  `test8` varchar(255) DEFAULT NULL,
  `test9` varchar(255) DEFAULT NULL,
  `test10` varchar(255) DEFAULT NULL,
  `test11` varchar(255) DEFAULT NULL,
  `test12` varchar(255) DEFAULT NULL,
  `test13` varchar(255) DEFAULT NULL,
  `test14` varchar(255) DEFAULT NULL,
  `test15` varchar(255) DEFAULT NULL,
  `test16` varchar(255) DEFAULT NULL,
  `test17` varchar(255) DEFAULT NULL,
  `test18` varchar(255) DEFAULT NULL,
  `test19` varchar(255) DEFAULT NULL,
  `test20` varchar(255) DEFAULT NULL,
  `test21` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=600001 DEFAULT CHARSET=utf8mb4 ;

