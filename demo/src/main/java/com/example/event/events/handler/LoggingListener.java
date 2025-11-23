package com.example.event.events.handler;

import com.example.event.events.dto.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingListener {
    private final LogRepository logRepository;
    // 트랜잭션이 필요없는 기능에서는 이것을 수행.
    @Async(value = "taskExecutor")
    @EventListener(value = { UserRegisteredEvent.class })
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("[Logging] 회원가입 로그 기록: userId=" + event.getUserId());
    }

    // EventListener는 Caller가 Transaction 보장이 필요없는 메서드일때, TransactionalEventListener는 Caller의 트랜잭션 전, 중, 후와 관련있도록 수행됨.
    // 트랜잭션이 필요한 기능에서는 Async 불가, TransactionalEventListener와 Transactional에서 새로운 트랜잭션 범위를 만들도록 설정
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            value = { UserRegisteredEvent.class },
            phase = TransactionPhase.BEFORE_COMMIT,
            fallbackExecution = true
    )
    public void handleUserRegistered2(UserRegisteredEvent event) {
        log.info("[Logging] 회원가입 로그 기록2: userId=" + event.getUserId());
        logRepository.save(Log.builder().message(event.getEmail()).build());
    }
}
