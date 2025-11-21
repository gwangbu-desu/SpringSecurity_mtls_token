package com.example.async.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public ThreadPoolTaskExecutor workerExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3); // 기본 스레드 개수
        executor.setMaxPoolSize(10); // 최대 스레드 개수
        executor.setQueueCapacity(50); // 큐에 쌓을 수 있는 작업 개수
        executor.setKeepAliveSeconds(30); // idle 스레드 유지 시간
        executor.setThreadNamePrefix("worker-"); // 모니터링용 스레드 이름

        // caller 스레드에서 요청 실행.
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 넘치면 예외 발생
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}
