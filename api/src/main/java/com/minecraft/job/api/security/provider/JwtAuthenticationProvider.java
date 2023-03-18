package com.minecraft.job.api.security.provider;

import com.minecraft.job.api.component.JwtComponent;
import com.minecraft.job.api.security.user.DefaultMcjUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtComponent jwtComponent;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();

        jwtComponent.validate(token);

        Long id = jwtComponent.getId(token);

        return new UsernamePasswordAuthenticationToken(new DefaultMcjUser(id), token, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

