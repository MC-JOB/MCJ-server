package com.minecraft.job.api.component;

import com.minecraft.job.api.properties.JwtProperties;
import com.minecraft.job.api.security.user.McjUserException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.minecraft.job.api.component.JwtType.ACCESS;
import static com.minecraft.job.api.component.JwtType.REFRESH;
import static com.minecraft.job.common.support.Preconditions.notNull;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class JwtComponent {
    private final JwtProperties jwtProperties;

    public String issue(Long id, String audience, JwtType type) {
        val now = new Date();
        val expiration = new Date(now.getTime() + jwtProperties.getTokenExpireTime(type));

        return Jwts.builder()
                .setSubject("MCJ User " + type.name() + " Api Token")
                .setIssuer("MCJ")
                .setIssuedAt(now)
                .setId(id.toString())
                .setAudience(audience)
                .setExpiration(expiration)
                .signWith(HS256, jwtProperties.getEncodedSecretKey())
                .compact();
    }

    public Long getId(String token) {
        try {
            return Long.valueOf(Jwts.parser().setSigningKey(jwtProperties.getEncodedSecretKey()).parseClaimsJws(token).getBody().getId());
        } catch (ExpiredJwtException ex) {
            return Long.valueOf(ex.getClaims().getId());
        }
    }

    public String getAudience(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtProperties.getEncodedSecretKey()).parseClaimsJws(token).getBody().getAudience();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims().getAudience();
        }
    }

    public String resolveToken(HttpServletRequest req, JwtType type) {
        String header = null;
        if (type == ACCESS) {
            header = req.getHeader("Authorization");
        } else if (type == REFRESH) {
            header = req.getHeader("X-Refresh-Token");
        }

        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public void isExpired(String token) {
        try {
            notNull(Jwts.parser().setSigningKey(jwtProperties.getEncodedSecretKey()).parseClaimsJws(token).getBody());

            throw new McjUserException("Not expired access token", UNAUTHORIZED);
        } catch (ExpiredJwtException ignored) {
        } catch (IllegalArgumentException ex) {
            throw new McjUserException("Invalid access token", UNAUTHORIZED);
        }
    }

    public void validate(String token, JwtType type) {
        try {
            notNull(Jwts.parser().setSigningKey(jwtProperties.getEncodedSecretKey()).parseClaimsJws(token).getBody());
        } catch (ExpiredJwtException ex) {
            throw new McjUserException("Expired " + type.name() + " token", UNAUTHORIZED);
        } catch (IllegalArgumentException ex) {
            throw new McjUserException("Invalid " + type.name() + " token", UNAUTHORIZED);
        }
    }
}
