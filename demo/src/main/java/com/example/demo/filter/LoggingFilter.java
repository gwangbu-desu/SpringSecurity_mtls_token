package com.example.demo.filter;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class LoggingFilter extends OncePerRequestFilter {
    @PostConstruct
    public void init() {
        System.out.println(">>> LoggingFilter bean created");
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("=== [LoggingFilter] Request Info ===");
        System.out.println("URI          : " + request.getRequestURI());
        System.out.println("Method       : " + request.getMethod());
        System.out.println("Remote Addr  : " + request.getRemoteAddr());
        System.out.println("Remote Host  : " + request.getRemoteHost());

        // 1) 두 키 모두 확인
        Object cert1 = request.getAttribute("javax.servlet.request.X509Certificate");
        Object cert2 = request.getAttribute("jakarta.servlet.request.X509Certificate");

        System.out.println("Attr javax.servlet.request.X509Certificate   : " + cert1);
        System.out.println("Attr jakarta.servlet.request.X509Certificate : " + cert2);

        // 2) 모든 attribute 이름 찍어보기
        System.out.println("---- All request attributes ----");
        var names = request.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            System.out.println(" - " + name + " = " + request.getAttribute(name));
        }
        System.out.println("--------------------------------");

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            System.out.println("Auth Name    : " + auth.getName());
            System.out.println("Authorities  : " + auth.getAuthorities());
            System.out.println("Auth Class   : " + auth.getClass().getSimpleName());
        } else {
            System.out.println("Auth         : NULL (not authenticated yet)");
        }

        System.out.println("====================================");

        filterChain.doFilter(request, response);
    }

}
