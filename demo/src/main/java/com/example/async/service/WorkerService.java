package com.example.async.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WorkerService {

    @Async("workerExecutor")
    public void doAsyncTask(int taskId) {
        log.info("Start task {} on thread {}", taskId, Thread.currentThread().getName());

        try {
            Thread.sleep(2000); // 실제 작업이라 가정
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("End task {} on thread {}", taskId, Thread.currentThread().getName());
    }
}
