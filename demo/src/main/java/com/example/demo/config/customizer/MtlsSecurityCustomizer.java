package com.example.demo.config.customizer;

import com.example.demo.config.CustomAuthDetails;
import com.example.demo.service.VehicleDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


@Configuration
@RequiredArgsConstructor
public class MtlsSecurityCustomizer {
    private final VehicleDetailsService vehicleDetailsService;

    @Bean
    public Customizer<HttpSecurity> mtlsCustomizer(){
        return http -> HttpSecurityUtils.wrap(h -> {
            h.x509(x509 -> x509
                .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
                .userDetailsService(vehicleDetailsService)
                .authenticationDetailsSource(CustomAuthDetails::new)
            );
        },http);
    }
}
