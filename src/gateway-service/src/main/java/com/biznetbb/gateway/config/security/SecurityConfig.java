package com.biznetbb.gateway.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.ReactiveAuthenticationManager;
//import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.beans.factory.annotation.Autowired;

//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private ReactiveUserDetailsService userDetailsService;
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
//        return http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/auth/**", "/biznetbb-api/auth/**", "/actuator/**").permitAll()
//                        .anyExchange().authenticated()
//                )
//                .httpBasic().disable()  // Disable HTTP Basic Auth
//                .formLogin().disable()  // Disable form login
//                .build();
//    }
//
//    @Bean
//    public ReactiveAuthenticationManager authenticationManager(){
//        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
//    }
//
//    @Bean
//    public UrlBasedCorsConfigurationSource corsConfigurationSource(){
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOriginPattern("*");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//}
