package com.wm.file;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wm.file.entity.UserEntity;
import com.wm.file.mapper.UserMapper;
import com.wm.file.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
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


