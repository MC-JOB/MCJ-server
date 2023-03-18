package com.minecraft.job.api.config;

import com.minecraft.job.api.security.provider.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationProvider jwtAuthenticationProvider,
            SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> jwtFilterConfigurer
    ) throws Exception {
        //TODO 로그인 페이지 설정 이후 구체적인 PATH 추가
        http.authorizeRequests()
                .anyRequest().permitAll();

        http.formLogin().disable();
        http.httpBasic().disable();
        http.cors().disable();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        http.authenticationProvider(jwtAuthenticationProvider);
        http.apply(jwtFilterConfigurer);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //TODO Front Page 추가 이후 CORS 설정 필요
}
