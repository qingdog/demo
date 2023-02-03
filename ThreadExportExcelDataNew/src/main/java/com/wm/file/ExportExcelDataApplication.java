package com.wm.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName: ExportExcelDataApplication
 * @Description: 启动类
 * @Author: xiaomage
 * @Date: 2022/5/3 19:01
 **/
@EnableCaching
@EnableScheduling
@SpringBootApplication
@EnableAsync
@MapperScan("com.wm.file.mapper")//扫描mapper接口所在的包
public class ExportExcelDataApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExportExcelDataApplication.class, args);
    }
}
