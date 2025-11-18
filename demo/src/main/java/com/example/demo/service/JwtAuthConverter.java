package com.example.demo.service;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


// JWT토큰을 Spring Security에서 사용할 Authenticate로 변환. -> 직접 getAuthenticate를 구현할 필요가 없어짐.
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // "ROLE_VEHICLE,ROLE_DIAGNOSTIC" 같은 문자열 가져오기
        String authClaim = jwt.getClaimAsString("auth");

        // null 방지
        if (authClaim == null || authClaim.isBlank()) {
            authClaim = "";
        }

        // "ROLE_..." 들을 GrantedAuthority 로 변환
        Collection<GrantedAuthority> authorities =
                Arrays.stream(authClaim.split(","))
                        .filter(s -> !s.isBlank())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return new JwtAuthenticationToken(jwt, authorities);
    }
}
