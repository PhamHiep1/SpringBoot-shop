package com.example.ShopSpring.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);    // min thread
        executor.setMaxPoolSize(10);    // max thread
        executor.setQueueCapacity(500); // queue capacity
        executor.setThreadNamePrefix("EmailThread-");
        executor.initialize();
        return executor;
    }
}
