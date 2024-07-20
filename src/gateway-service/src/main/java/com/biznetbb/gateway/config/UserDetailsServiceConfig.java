package com.biznetbb.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserDetailsServiceConfig {

    public ReactiveUserDetailsService userDetailsService(){
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("admin"))
            .roles("administrator")
            .build();
        return new MapReactiveUserDetailsService(user);
    }

    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
