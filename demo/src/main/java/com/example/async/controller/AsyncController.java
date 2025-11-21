package com.example.async.controller;

import com.example.async.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;

@RequiredArgsConstructor
@RestController
public class AsyncController {

    private final WorkerService workerService;

    @GetMapping("/async-test")
    public String runAsyncTasks(@RequestParam(defaultValue = "10") int count) {
        for (int i = 1; i <= count; i++) {
            System.out.println(i);
            workerService.doAsyncTask(i);
        }
        return "Triggered " + count + " async tasks";
    }
}
