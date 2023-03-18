package com.minecraft.job.api.security.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final OncePerRequestFilter jwtFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
