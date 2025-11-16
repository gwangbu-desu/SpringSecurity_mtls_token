package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VehicleController {
    @GetMapping("/vehicle/status")
    public String status(Authentication auth) {
        return "Authenticated Vehicle: " + auth.getName();
    }
}
