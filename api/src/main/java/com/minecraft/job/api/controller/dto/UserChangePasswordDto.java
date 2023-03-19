package com.minecraft.job.api.controller.dto;

public class UserChangePasswordDto {

    public record UserChangePasswordRequest(
            String password, String newPassword
    ) {
    }
}
