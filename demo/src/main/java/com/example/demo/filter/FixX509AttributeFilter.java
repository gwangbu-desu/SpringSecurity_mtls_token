package com.example.demo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Spring Boot 3 (Jakarta EE 9+) 환경에서 X.509 인증을 처리하기 위한 필터.
 * Tomcat이 'jakarta.servlet.request.X509Certificate' 속성에 저장한 인증서를
 * Spring Security의 X509AuthenticationFilter가 기대하는 'javax.servlet.request.X509Certificate' 속성으로 복사합니다.
 */
public class FixX509AttributeFilter extends OncePerRequestFilter {

    private static final String JAKARTA_X509_CERTIFICATE_ATTRIBUTE = "jakarta.servlet.request.X509Certificate";
    private static final String JAVAX_X509_CERTIFICATE_ATTRIBUTE = "javax.servlet.request.X509Certificate";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getAttribute(JAVAX_X509_CERTIFICATE_ATTRIBUTE) == null) {
            request.setAttribute(JAVAX_X509_CERTIFICATE_ATTRIBUTE, request.getAttribute(JAKARTA_X509_CERTIFICATE_ATTRIBUTE));
        }

        filterChain.doFilter(request, response);
    }
}