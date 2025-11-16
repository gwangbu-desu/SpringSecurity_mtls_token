# Vehicle Authentication Server (mTLS + JWT)

## Overview

This project implements an mTLS-based vehicle authentication server
using Spring Boot 3, Java 21, Spring Security, and JWT. Vehicles
authenticate using client certificates, receive a JWT token, and then
access protected APIs.

## Tech Stack

-   Java 21
-   Spring Boot 3.x (Web, Security, OAuth2 Resource Server)
-   mTLS (X.509 Client Certificate Authentication)
-   JWT (JJWT)
-   Tomcat Web Server
-   PKCS12 Keystore/Truststore

## Project Structure

    src/main/java
      └── com.example.vehicleauth
            ├── config
            │     ├── SecurityConfig.java
            │     └── CertExtractor.java
            ├── controller
            │     ├── AuthController.java
            │     └── VehicleController.java
            ├── service
            │     ├── JwtService.java
            │     └── VehicleDetailsService.java
            └── VehicleAuthApplication.java
    src/main/resources
      ├── application.yml
      ├── server-keystore.p12
      └── server-truststore.p12

## Dependencies (Gradle)

``` gradle
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.springframework.boot:spring-boot-starter-oauth2-resource-server'

implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.5'

compileOnly 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok'
```

## 1. mTLS Configuration

`application.yml`:

``` yaml
server:
  ssl:
    enabled: true
    key-store: classpath:server-keystore.p12
    key-store-password: changeit
    key-store-type: PKCS12

    trust-store: classpath:server-truststore.p12
    trust-store-password: changeit
    trust-store-type: PKCS12

    client-auth: need
```

## 2. X.509 Authentication

``` java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .x509(x509 -> x509
            .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
            .userDetailsService(vehicleDetailsService)
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/token").authenticated()
            .anyRequest().hasRole("VEHICLE")
        );
    return http.build();
}
```

## 3. JWT Token Issuance

``` java
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;

    @PostMapping("/auth/token")
    public Map<String, String> issueToken(Authentication authentication) {
        String vehicleId = authentication.getName();
        String token = jwtService.generateToken(vehicleId);
        return Map.of("token", token);
    }
}
```

## 4. JWT Service

``` java
@Service
public class JwtService {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String vehicleId) {
        return Jwts.builder()
                .setSubject(vehicleId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000))
                .claim("role", "VEHICLE")
                .signWith(key)
                .compact();
    }
}
```

## 5. Vehicle API

``` java
@RestController
public class VehicleController {

    @GetMapping("/vehicle/status")
    public String status(Authentication auth) {
        return "Vehicle " + auth.getName() + " status OK";
    }
}
```

## 6. Testing

### mTLS connection test

``` bash
curl -v --cert vehicle-cert.pem --key vehicle-key.pem https://localhost:8443/auth/token
```

### JWT API test

``` bash
curl -H "Authorization: Bearer <TOKEN>" https://localhost:8443/vehicle/status
```
