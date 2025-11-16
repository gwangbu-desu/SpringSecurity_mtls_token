package com.example.demo.config;

import com.example.demo.controller.VehicleController;
import com.example.demo.filter.FixX509AttributeFilter;
import com.example.demo.filter.LoggingFilter;
import com.example.demo.service.VehicleDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;

@Configuration
@Order(1)
@RequiredArgsConstructor
public class MutualTlsSecurityConfig {
    private final VehicleDetailsService vehicleDetailsService;

    @Bean
    public SecurityFilterChain mtlsFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/auth/token") // 1. 이 필터체인은 /auth/token 경로에만 적용하도록 활성화
                // FixX509AttributeFilter가 가장 먼저 실행되어 인증서 속성 문제를 해결합니다.
                .addFilterBefore(new FixX509AttributeFilter(), X509AuthenticationFilter.class)
                .addFilterAfter(new LoggingFilter(), FixX509AttributeFilter.class) // LoggingFilter는 그 다음에 실행됩니다.
                .csrf(csrf -> csrf.disable())
                .x509(x509 -> x509
                        .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
                        .userDetailsService(vehicleDetailsService)
                        // 인증 상세 정보로 CustomAuthDetails 객체를 생성하도록 지정
                        .authenticationDetailsSource(CustomAuthDetails::new)
                )
                .sessionManagement(session -> session
                        // REST API이므로 세션을 STATELESS로 설정
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated() // securityMatcher로 이미 경로가 한정되었으므로, 모든 요청에 인증 요구
                );

        System.out.println(">>> Mtls filter chain active");
        return http.build();
    }
}