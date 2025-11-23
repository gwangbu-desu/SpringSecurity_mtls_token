package com.example.event.events;

import com.example.event.events.dto.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final ApplicationEventPublisher publisher;
    public void register(String email) {
        Long userId = 1L;

        log.info("[UserService] 회원가입 완료: " + email);

        UserRegisteredEvent event = new UserRegisteredEvent(userId, email);
        publisher.publishEvent(event);
    }
}
