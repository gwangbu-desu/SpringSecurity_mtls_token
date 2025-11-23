package com.example.event.controller;

import com.example.event.events.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final UserService userService;

    @PostMapping("/register")
    public String register(@RequestParam String email) {
        userService.register(email);
        return "OK";
    }

    @PostMapping("/register2")
    public String register2(@RequestParam String email) {
        userService.register2(email);
        return "OK";
    }
}
