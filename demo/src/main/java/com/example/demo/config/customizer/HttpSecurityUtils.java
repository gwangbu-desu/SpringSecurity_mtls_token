package com.example.demo.config.customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class HttpSecurityUtils {

    public static void wrap(ThrowingConsumer<HttpSecurity> consumer, HttpSecurity http) {
        try {
            consumer.accept(http);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public interface ThrowingConsumer<T> {
        void accept(T t) throws Exception;
    }
}

