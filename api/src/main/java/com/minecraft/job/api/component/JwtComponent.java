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

    public String issue(Long id, String audience, JwtType type) {
        val now = new Date();
        val expiration = new Date(now.getTime() + jwtProperties.getTokenExpireTime(type));

        return Jwts.builder()
                .setSubject("MCJ User" + type.name() + "Api Token")
                .setIssuer("MCJ")
                .setIssuedAt(now)
                .setId(id.toString())
                .setAudience(audience)
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(UTF_8)), HS256)
                .compact();
    }

    public Long getId(String token) {
        try {
            return Long.valueOf(Jwts.parserBuilder().setSigningKeyResolver(getSigningKeyResolver()).build().parseClaimsJws(token).getBody().getId());
        } catch (ExpiredJwtException ex) {
            return Long.valueOf(ex.getClaims().getId());
        }
    }

    public String getAudience(String token) {
        try {
            return Jwts.parserBuilder().setSigningKeyResolver(getSigningKeyResolver()).build().parseClaimsJws(token).getBody().getAudience();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims().getAudience();
        }
    }

    public String resolveToken(HttpServletRequest req, JwtType type) {
        String header = null;
        if (type == JwtType.ACCESS) {
            header = req.getHeader("Authorization");
        } else if (type == JwtType.REFRESH) {
            header = req.getHeader("X-Refresh-Token");
        }

        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public void isExpired(String token) {
        try {
            notNull(Jwts.parserBuilder().setSigningKeyResolver(getSigningKeyResolver()).build().parseClaimsJws(token));

            throw new McjUserException("Not expired access token", UNAUTHORIZED);
        } catch (ExpiredJwtException ignored) {
        }
    }

    public void validate(String token, JwtType type) {
        try {
            notNull(Jwts.parserBuilder().setSigningKeyResolver(getSigningKeyResolver()).build().parseClaimsJws(token));
        } catch (ExpiredJwtException ex) {
            throw new McjUserException("Expired " + type.name() + " token", UNAUTHORIZED);
        } catch (IllegalArgumentException ex) {
            throw new McjUserException("Invalid " + type.name() + " token", UNAUTHORIZED);
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
