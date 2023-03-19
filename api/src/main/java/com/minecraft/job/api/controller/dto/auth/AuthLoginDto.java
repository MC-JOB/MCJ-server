package com.minecraft.job.api.controller.dto.auth;

import org.springframework.data.util.Pair;

public class AuthLoginDto {

    public record AuthLoginRequest(String email, String password) {
    }

    public record AuthLoginResponse(String accessToken, String refreshToken) {

        public static AuthLoginResponse create(Pair<String, String> loginTokens) {
            return new AuthLoginResponse(
                    loginTokens.getFirst(),
                    loginTokens.getSecond()
            );
        }
    }
}
