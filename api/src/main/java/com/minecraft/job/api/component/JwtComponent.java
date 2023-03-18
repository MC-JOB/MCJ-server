package com.minecraft.job.api.component;

import com.minecraft.job.api.properties.JwtProperties;
import com.minecraft.job.api.security.user.McjUserException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

import static com.minecraft.job.common.support.Preconditions.notNull;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class JwtComponent {
    private final JwtProperties jwtProperties;

    public String issue(Long id, String audience, Date now) {
        val expiration = new Date(now.getTime() + jwtProperties.getAccessTokenExpireTime());

        return Jwts.builder()
                .setSubject("MCJ User Api Token")
                .setIssuer("MCJ")
                .setIssuedAt(now)
                .setId(id.toString())
                .setAudience(audience)
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(UTF_8)), HS256)
                .compact();
    }

    public Long getId(String accessToken) {
        try {
            return Long.valueOf(Jwts.parserBuilder().setSigningKeyResolver(getSigningKeyResolver()).build().parseClaimsJws(accessToken).getBody().getId());
        } catch (ExpiredJwtException ex) {
            return Long.valueOf(ex.getClaims().getId());
        }
    }

    public String resolveToken(HttpServletRequest req) {
        val token = req.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer")) {
            return token.substring(7);
        } else {
            return null;
        }
    }

    public Boolean isExpired(String accessToken) {
        try {
            notNull(Jwts.parserBuilder().setSigningKeyResolver(getSigningKeyResolver()).build().parseClaimsJws(accessToken));

            return false;
        } catch (ExpiredJwtException ex) {
            return true;
        }
    }

    public void validate(String token) {
        try {
            notNull(Jwts.parserBuilder().setSigningKeyResolver(getSigningKeyResolver()).build().parseClaimsJws(token));
        } catch (ExpiredJwtException ex) {
            throw new McjUserException("Expired access token", UNAUTHORIZED);
        } catch (IllegalArgumentException ex) {
            throw new McjUserException("Invalid access token", UNAUTHORIZED);
        }
    }

    private SigningKeyResolver getSigningKeyResolver() {
        return new SigningKeyResolverAdapter() {
            @Override
            public Key resolveSigningKey(JwsHeader header, Claims claims) {
                return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(UTF_8));
            }
        };
    }
}
