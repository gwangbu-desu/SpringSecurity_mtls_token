package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
