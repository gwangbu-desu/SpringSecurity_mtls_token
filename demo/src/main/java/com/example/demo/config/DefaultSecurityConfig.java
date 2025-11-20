package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true) // 보안 설정을 총괄하고 디버그 모드를 켭니다.
@RequiredArgsConstructor
public class DefaultSecurityConfig {
    private final Customizer<HttpSecurity> commonCustomizer;
    private final Customizer<HttpSecurity> mtlsCustomizer;
    private final Customizer<HttpSecurity> jwtCustomizer;

    // ========================================================
    // 1) mTLS + JWT 기반 고보안 API (/secure/**) 둘다 필요한 경우가 있을까? MQTT는 mTls만, jwt 요청은 HTTPS + JWT사용하면 될듯싶은데
    // ========================================================
    @Bean
    public SecurityFilterChain highSecureChain(HttpSecurity http) throws Exception {

        // 공통 설정
        commonCustomizer.customize(http);

        // URL 매칭
        http.securityMatcher("/secure/**");

        // 인증 방식: mTLS + JWT 모두 적용
        mtlsCustomizer.customize(http);
        jwtCustomizer.customize(http);

        // 접근 제어
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
        System.out.println(">>> Secure Chain (mTLS + JWT) active");
        return http.build();
    }
    // ========================================================
    // 2) mTLS만 적용되는 API → JWT 발급용 엔드포인트 (/mtls/auth/token/**)
    // ========================================================
    @Bean
    public SecurityFilterChain mtlsOnlyChain(HttpSecurity http) throws Exception {
        commonCustomizer.customize(http);
        http.securityMatcher("/auth/token/**");

        // 인증: mTLS만 적용
        mtlsCustomizer.customize(http);
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
        System.out.println(">>> mTLS Chain (for JWT issuance) active");

        return http.build();
    }
    // ========================================================
    // 3) JWT만 적용되는 API → JWT 사용 엔드포인트 (/api/**) + HTTPS로 보안적용
    // ========================================================
    @Bean
    public SecurityFilterChain jwtOnlyChain(HttpSecurity http) throws Exception {
        commonCustomizer.customize(http);
        http.securityMatcher("/api/**");

        // 인증: mTLS만 적용
        jwtCustomizer.customize(http);
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
        System.out.println(">>> mTLS Chain (for JWT issuance) active");

        return http.build();
    }
    // ========================================================
    // 4) 기본 Public Chain (서명용 요청)
    // ========================================================
    @Bean
    public SecurityFilterChain publicChain(HttpSecurity http) throws Exception {
        commonCustomizer.customize(http);
        http.securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        System.out.println(">>> Public Chain active");
        return http.build();
    }
}
