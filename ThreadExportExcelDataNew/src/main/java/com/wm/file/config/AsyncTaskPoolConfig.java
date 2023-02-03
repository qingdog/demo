package com.wm.file.config;

import org.apache.poi.ss.formula.functions.Na;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
public class AsyncTaskPoolConfig {

    @Bean("taskExecutor")
    public ThreadPoolExecutor taskExecutor() {
        int i = Runtime.getRuntime().availableProcessors();
        System.out.println("系统最大线程数：" + i);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(i,
                i+1, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<>(200),new NamedThreadFactory("execl导出线程池"));
        System.out.println("execl导出线程池初始化完毕-------------");
        return threadPoolExecutor;
    }
}
