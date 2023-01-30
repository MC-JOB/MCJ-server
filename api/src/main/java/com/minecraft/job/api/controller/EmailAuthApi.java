package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.EmailAuthValidateDto.EmailAuthValidateRequest;
import com.minecraft.job.api.controller.dto.EmailAuthValidateDto.EmailAuthValidateResponse;
import com.minecraft.job.common.emailauth.domain.CodeNotValidException;
import com.minecraft.job.common.emailauth.domain.TimeExceededException;
import com.minecraft.job.common.emailauth.domain.TryCountExceededException;
import com.minecraft.job.common.emailauth.service.EmailAuthService;
import com.minecraft.job.common.support.MinecraftJobException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.minecraft.job.api.controller.dto.EmailAuthIssueDto.EmailAuthIssueRequest;
import static com.minecraft.job.common.support.ErrorCode.*;

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
        try {
            boolean result = emailAuthService.validate(req.email(), req.code());
            return new EmailAuthValidateResponse(result);
        } catch (TimeExceededException e) {
            throw new MinecraftJobException(CODE_VALID_TIME_EXCEEDED);
        } catch (TryCountExceededException e) {
            throw new MinecraftJobException(TRY_COUNT_EXCEEDED);
        } catch (CodeNotValidException e) {
            throw new MinecraftJobException(CODE_NOT_VALID);
        }
    }
}
