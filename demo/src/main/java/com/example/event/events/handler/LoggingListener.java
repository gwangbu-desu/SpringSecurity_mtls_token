package com.example.event.events.handler;

import com.example.event.events.dto.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingListener {
    @Async(value = "taskExecutor")
    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("[Logging] 회원가입 로그 기록: userId=" + event.getUserId());
    }
}
