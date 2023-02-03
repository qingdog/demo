package com.wm.file;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wm.file.entity.MsgClient;
import com.wm.file.entity.UserEntity;
import com.wm.file.mapper.UserMapper;
import com.wm.file.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExportExcelDataApplication.class)//springboot注解
public class TestDemo {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;
    @Test
    public void test1(){
        List<UserEntity> userEntityList = new ArrayList<>();
        for (int i = 0; i < 200000; i++) {  //测试数据
            UserEntity client = new UserEntity();
            //client.setBirthday(new Date());
            client.setName("小明xxxsxsxsxsxsxsxsxsxsx" + i);
            client.setPhone("18797" + i);
            client.setCeateBy("JueYue");
            client.setRemark("测试" + i);
            client.setTest1("测试数据1"+ i);
            client.setTest2("测试数据2"+ i);
            client.setTest3("测试数据3"+ i);
            client.setTest4("测试数据4"+ i);
            client.setTest5("测试数据5"+ i);
            client.setTest6("测试数据6"+ i);
            client.setTest7("测试数据7"+ i);
            client.setTest8("测试数据8"+ i);
            client.setTest9("测试数据9"+ i);
            client.setTest10("测试数据10"+ i);
            client.setTest11("测试数据11"+ i);
            client.setTest12("测试数据12"+ i);
            client.setTest13("测试数据13"+ i);
            client.setTest14("测试数据14"+ i);
            client.setTest15("测试数据15"+ i);
            client.setTest16("测试数据16"+ i);
            client.setTest17("测试数据17"+ i);
            client.setTest18("测试数据18"+ i);
            client.setTest19("测试数据19"+ i);
            client.setTest20("测试数据20"+ i);
            client.setTest21("测试数据21"+ i);
            userEntityList.add(client);
        }
        userService.saveBatch(userEntityList);
    }

    @Test
    public void test(){
        List<UserEntity> userEntityList = new ArrayList<>();
            UserEntity client = new UserEntity();
            //client.setBirthday(new Date());
            client.setName("小明xxxsxsxsxsxsxsxsxsxsx");
            client.setPhone("18797");
            client.setCeateBy("JueYue");
            userEntityList.add(client);
        userService.saveBatch(userEntityList);
    }

    @Test
    public void test2 (){
        PageHelper.startPage(1,100000);
        List<UserEntity> userEntities = userMapper.selectAll();
        PageInfo<UserEntity> userEntityPageInfo = new PageInfo<>(userEntities);
        System.out.println(userEntityPageInfo.getList());
        System.out.println(userEntityPageInfo.getPages());
    }
}


   /* CREATE TABLE `user` (
        `id` bigint(20) NOT NULL AUTO_INCREMENT,
        `name` varchar(255) DEFAULT NULL,
        `phone` varchar(255) DEFAULT NULL,
        `ceate_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
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
        ) ENGINE=InnoDB AUTO_INCREMENT=400001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;*/


