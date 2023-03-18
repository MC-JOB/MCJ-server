package com.minecraft.job.api.properties;

import com.minecraft.job.api.component.JwtType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Getter
@Component
public class JwtProperties {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.accessTokenExpireTime}")
    private Long accessTokenExpireTime;

    @Value("${jwt.refreshTokenExpireTime}")
    private Long refreshTokenExpireTime;

    public byte[] getEncodedSecretKey() {
        return Base64.getDecoder().decode(secretKey);
    }

    public Long getTokenExpireTime(JwtType jwtType) {
        Long expireTime = 0L;

        switch (jwtType) {
            case ACCESS -> expireTime = accessTokenExpireTime;

            case REFRESH -> expireTime = refreshTokenExpireTime;
        }

        return expireTime;
    }
}
