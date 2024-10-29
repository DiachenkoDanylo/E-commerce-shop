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
    private final UserAuthProvider userAuthProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserAuthProvider userAuthProvider) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)
                .sessionManagement(customize -> customize.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/items/**").hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                        .requestMatchers("/review/**").hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                        .requestMatchers("/bucket/**").hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                        .requestMatchers("/category/**").hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                        .requestMatchers("/login", "/register").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

}


