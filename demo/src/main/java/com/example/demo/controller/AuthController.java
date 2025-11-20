package com.example.demo.controller;

import com.example.demo.service.CertificateService;
import com.example.demo.service.JwtService;
import com.example.demo.service.VehicleDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final CertificateService certificateService;
    private final VehicleDetailsService vehicleDetailsService;

    /**
     * mTLS 인증 성공 후 JWT 토큰을 발급하는 엔드포인트
     */
    @PostMapping("/auth/token")
    public Map<String, String> issueToken(Authentication auth) {
        String accessToken = jwtService.createToken(auth);
        return Map.of("accessToken", accessToken);
    }

    /**
     * 디바이스 등록 및 CSR 서명을 위한 엔드포인트
     * @param request deviceId와 csr을 포함하는 DTO
     * @return 서명된 인증서 (PEM 형식)
     */
    @PostMapping(value = "/auth/sign-csr", produces = MediaType.TEXT_PLAIN_VALUE)
    public String signCsr(@RequestBody CsrRequest request) throws Exception {
        // 1. 사전 등록된 디바이스 정보 확인
        // 이 단계에서는 DB에서 deviceId를 조회하여, 인증서 발급 대기 상태인지,
        // 그리고 해당 디바이스에 부여될 정확한 CN(Common Name)이 무엇인지 확인해야 합니다.
        // 여기서는 VehicleDetailsService에 해당 기능이 있다고 가정합니다.
        UserDetails deviceDetails = vehicleDetailsService.loadUserByUsername(request.getDeviceId());
        String expectedCn = deviceDetails.getUsername(); // DB에 저장된 정확한 CN 값

        // 2. CSR 서명 요청
        // CSR의 CN이 DB에 저장된 값과 일치하는지 CertificateService 내부에서 검증합니다.
        String signedCertificate = certificateService.signCsr(request.getCsr(), expectedCn);

        // 3. (선택) DB 상태 업데이트
        // 인증서 발급이 완료되었으므로, DB에서 해당 디바이스의 상태를 'ACTIVE'로 변경할 수 있습니다.
        // vehicleDetailsService.activateDevice(request.getDeviceId());

        return signedCertificate;
    }

    // --- DTO for CSR request ---
    public static class CsrRequest {
        private String deviceId;
        private String csr;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getCsr() {
            return csr;
        }

        public void setCsr(String csr) {
            this.csr = csr;
        }
    }
}
