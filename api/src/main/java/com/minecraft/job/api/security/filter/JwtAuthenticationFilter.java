package com.minecraft.job.api.security.filter;

import com.minecraft.job.api.security.component.JwtComponent;
import com.minecraft.job.api.security.user.McjUserException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtComponent jwtComponent;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        val accessToken = jwtComponent.resolveToken(request);

        try {
            if (nonNull(accessToken)) {
                val authenticationToken = new UsernamePasswordAuthenticationToken(accessToken, "", new ArrayList<>());

                val authentication = authenticationManager.authenticate(authenticationToken);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (McjUserException ex) {
            ex.printStackTrace();

            SecurityContextHolder.clearContext();

            response.sendError(ex.getStatus().value(), ex.getMessage());

            return;
        }

        filterChain.doFilter(request, response);
    }
}
