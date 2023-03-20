package com.minecraft.job.api.controller.dto.emailauth;

public class EmailAuthValidateDto {

    public record EmailAuthValidateRequest(String email, String code) {
    }

    public record EmailAuthValidateResponse(boolean isValidate) {

    }
}
