package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;

import java.security.cert.X509Certificate;
import java.util.Collections;

public class CustomAuthDetails extends PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails {

    private final X509Certificate[] certificates;

    public CustomAuthDetails(HttpServletRequest request) {
        // 상위 클래스 생성자는 request와 authorities를 필요로 합니다.
        // 이 시점에는 아직 authorities를 알 수 없으므로, 빈 리스트를 전달합니다.
        // authorities는 나중에 UserDetailsService에서 로드됩니다.
        super(request, Collections.emptyList());
        this.certificates = (X509Certificate[]) request.getAttribute("jakarta.servlet.request.X509Certificate");
    }

    public X509Certificate[] getCertificates() {
        return certificates;
    }
}