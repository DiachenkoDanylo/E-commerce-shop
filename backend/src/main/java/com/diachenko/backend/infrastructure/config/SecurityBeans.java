package com.diachenko.backend.infrastructure.config;
/*  E-commerce-shop
    02.10.2024
    @author DiachenkoDanylo
*/

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityBeans {

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "CLIENT";

    private static final String[] AUTHORIZED_ROUTES = {
            "/items/**", "/review/**", "/bucket/**", "/category/**", "/wishlist/**", "/images/**"
    };

    private static final String[] PUBLIC_ROUTES = {
            "/login", "/register",
    };

    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-resources",
    };



    private final UserAuthProvider userAuthProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserAuthProvider userAuthProvider) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)
                .sessionManagement(customize -> customize.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers(PUBLIC_ROUTES).permitAll()
                        .requestMatchers(AUTHORIZED_ROUTES).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                        .anyRequest().authenticated());
        return http.build();
    }

}


