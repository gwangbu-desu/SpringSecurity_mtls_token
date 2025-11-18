package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
    private final JwtDecoder jwtDecoder;

    @GetMapping("/hello")
    public String hello(){
        return "Hello World!";
    }
    @GetMapping("/status")
    public String status(Authentication auth) {

        // JWT 문자열
        Jwt jwt = (Jwt)auth.getPrincipal();

        // JWT 디코드
//        var decoded = jwtDecoder.decode(token);

        assert jwt.getIssuedAt() != null;
        assert jwt.getExpiresAt() != null;
        return Map.of(
                "subject", jwt.getSubject(),
                "auth", jwt.getClaim("auth"),
                "issuedAt", jwt.getIssuedAt(),
                "expiresAt", jwt.getExpiresAt()
        ).toString();
    }
}
