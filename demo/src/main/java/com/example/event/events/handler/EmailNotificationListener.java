package com.example.event.events.handler;

import com.example.event.events.dto.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailNotificationListener {
    @Async(value = "taskExecutor")
    @EventListener(value = { UserRegisteredEvent.class })
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("[EmailNotification] 이메일 전송: " + event.getEmail());
    }
}
