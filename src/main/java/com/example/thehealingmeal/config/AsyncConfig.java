package com.example.thehealingmeal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4); // 기본 스레드 수
        taskExecutor.setMaxPoolSize(6); // 최대 스레드 수
        taskExecutor.setQueueCapacity(100); // Queue 사이즈
        taskExecutor.setThreadNamePrefix("Thread-");

        taskExecutor.initialize();
        return taskExecutor;
    }
}
