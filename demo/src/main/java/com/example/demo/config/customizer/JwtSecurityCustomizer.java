package com.example.demo.config.customizer;

import com.example.demo.service.JwtAuthConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
@RequiredArgsConstructor
public class JwtSecurityCustomizer {
    private final JwtDecoder jwtDecoder;
    private final JwtAuthConverter jwtAuthConverter;
    @Bean
    public Customizer<HttpSecurity> jwtCustomizer() {
        return http -> HttpSecurityUtils.wrap( h -> {
            h.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder)
                    .jwtAuthenticationConverter(jwtAuthConverter)
                )
            );
        },http);
    }
}
