package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.EmailAuthValidateDto.EmailAuthValidateRequest;
import com.minecraft.job.api.controller.dto.EmailAuthValidateDto.EmailAuthValidateResponse;
import com.minecraft.job.common.emailauth.service.EmailAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.minecraft.job.api.controller.dto.EmailAuthIssueDto.EmailAuthIssueRequest;

@RestController
@RequestMapping("/email-auth")
@RequiredArgsConstructor
public class EmailAuthApi {

    private final EmailAuthService emailAuthService;

    @PostMapping("/issue")
    public void issue(@RequestBody EmailAuthIssueRequest req) {
        emailAuthService.issue(req.email());
    }

    @PostMapping("/validate")
    public EmailAuthValidateResponse issue(@RequestBody EmailAuthValidateRequest req) {
        boolean result = emailAuthService.validate(req.email(), req.code());

        return new EmailAuthValidateResponse(result);
    }
}
