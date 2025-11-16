package com.example.demo.controller;

import com.example.demo.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;

    @PostMapping("/auth/token")
    public Map<String, String> issueToken(Authentication auth) {

//        String vehicleId = auth.getName(); // CN 값 (VEHICLE-1234)
        String accessToken = jwtService.createToken(auth);

        return Map.of("accessToken", accessToken);
    }
}
