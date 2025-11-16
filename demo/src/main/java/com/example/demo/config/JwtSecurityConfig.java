package com.example.demo.config;

import com.example.demo.filter.LoggingFilter;
import com.example.demo.service.JwtAuthConverter;
import com.example.demo.service.JwtService;
import com.example.demo.service.VehicleDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(2)
@RequiredArgsConstructor
public class JwtSecurityConfig {
    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain HttpSecurity(HttpSecurity http) throws Exception {

        http
                .addFilterBefore(new LoggingFilter(), UsernamePasswordAuthenticationFilter.class)

                .securityMatcher("/**")
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth ->
                        oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthConverter()))
                )
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().hasRole("VEHICLE")
                );
        System.out.println(">>> JWT filter chain active");
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(jwtService.getKey()).build();
    }
}