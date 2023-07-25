package com.drizh2.instazoo.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomPasswordEncoder {

    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

}
