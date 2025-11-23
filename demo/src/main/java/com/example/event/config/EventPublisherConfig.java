package com.example.event.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//@Configuration
public class EventPublisherConfig {
    // EventListener 모든 리스너에 대한 기본 스레드풀 적용.
    // -> 실무에서는 절대 이렇게 사용하면안된다고함.
    // EventListener 마다 taskExecutor를 할당하는 식으로 사용하기.
    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster customEventMulticaster() {
        SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setThreadNamePrefix("EventPublish-");
        executor.initialize();

        multicaster.setTaskExecutor(executor);
        return multicaster;
    }
}
