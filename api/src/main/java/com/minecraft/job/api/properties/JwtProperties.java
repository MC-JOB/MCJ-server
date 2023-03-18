package com.minecraft.job.api.properties;

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

    public byte[] getEncodedSecretKey() {
        return Base64.getDecoder().decode(secretKey);
    }
}
