package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;

import java.security.cert.X509Certificate;
import java.util.Collections;

public class CustomAuthDetails extends PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails {

    private final X509Certificate[] certificates;

    public CustomAuthDetails(HttpServletRequest request) {
        super(request, Collections.emptyList());
//        this.certificates = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");

        this.certificates = (X509Certificate[]) request.getAttribute("jakarta.servlet.request.X509Certificate");
    }

    public X509Certificate[] getCertificates() {
        return certificates;
    }
}