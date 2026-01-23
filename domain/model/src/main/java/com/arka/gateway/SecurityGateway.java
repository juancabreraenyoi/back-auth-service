package com.arka.gateway;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface SecurityGateway {

    PasswordEncoder passwordEncoder();
}
