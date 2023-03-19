package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.AuthLoginDto.AuthLoginRequest;
import com.minecraft.job.api.controller.dto.AuthLoginDto.AuthLoginResponse;
import com.minecraft.job.api.service.AuthAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final AuthAppService authAppService;

    @PostMapping("/login")
    public AuthLoginResponse login(
            @RequestBody AuthLoginRequest req
    ) {
        Pair<String, String> loginTokens = authAppService.login(req.email(), req.password());

        return AuthLoginResponse.create(loginTokens);
    }
}
