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
    // Queue是java自己的队列，是同步安全的
    public static Queue<Map<String, Object>> queue;

    static {
        // 一个基于链接节点的无界线程安全的队列
        queue = new ConcurrentLinkedQueue<>();
    }
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
