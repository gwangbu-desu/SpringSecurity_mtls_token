package com.example.async.monitor;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Component
@RequiredArgsConstructor
public class ExecutorMonitor {

    private final ThreadPoolTaskExecutor workerExecutor;

    @Scheduled(fixedRate = 2000) // 2초마다 모니터링
    public void monitorThreadPool() {

        ThreadPoolExecutor tpe = workerExecutor.getThreadPoolExecutor();

        int poolSize = tpe.getPoolSize();
        int activeCount = tpe.getActiveCount();
        long completedTaskCount = tpe.getCompletedTaskCount();
        int queueSize = tpe.getQueue().size();
        int corePoolSize = tpe.getCorePoolSize();
        int maxPoolSize = tpe.getMaximumPoolSize();

        System.out.println("=== ThreadPool Status ===");
        System.out.println("corePoolSize: " + corePoolSize);
        System.out.println("maxPoolSize : " + maxPoolSize);
        System.out.println("poolSize    : " + poolSize);
        System.out.println("activeCount : " + activeCount);
        System.out.println("queueSize   : " + queueSize);
        System.out.println("completed   : " + completedTaskCount);
        System.out.println("==========================");
    }
}
