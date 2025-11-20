package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// [TODO]이 시점에 DB에서 권한을 찾고, 부여
// [TODO]하나의 디바이스에도 여러 인증서가 존재하므로 vehicleId로 조회하는 것은 위험.

@Service
@Slf4j
public class VehicleDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String vehicleId) throws UsernameNotFoundException {
        log.debug("Attemping to load user by usesrname: {}", vehicleId);
        return User.withUsername(vehicleId)
                .password("")
                .roles("VEHICLE").build();
    }
}
