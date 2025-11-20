package com.example.jpa;

import com.example.jpa.service.JpaTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


}
