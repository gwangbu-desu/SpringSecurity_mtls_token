//package com.example.demo.config;
//
//import com.example.demo.filter.FixX509AttributeFilter;
//import com.example.demo.filter.LoggingFilter;
//import com.example.demo.service.VehicleDetailsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
//
//@Configuration
//@Order(1)
//@RequiredArgsConstructor
//public class MutualTlsSecurityConfig {
//    private final VehicleDetailsService vehicleDetailsService;
//
//    @Bean
//    public SecurityFilterChain mtlsFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/auth/token")
//                .apply(new CommonHttpConfigurer()).and()
//                .addFilterBefore(new FixX509AttributeFilter(), X509AuthenticationFilter.class)
//                .addFilterAfter(new LoggingFilter(), FixX509AttributeFilter.class)
//                .x509(x509 -> x509
//                        .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
//                        .userDetailsService(vehicleDetailsService)
//                        .authenticationDetailsSource(CustomAuthDetails::new));
//
//        System.out.println(">>> Mtls filter chain active");
//        return http.build();
//    }
//}