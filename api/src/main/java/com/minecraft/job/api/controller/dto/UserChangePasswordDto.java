package com.minecraft.job.api.controller.dto;

public class UserChangePasswordDto {

    public record UserChangePasswordRequest(
            Long userId, String password, String newPassword
    ) {
    }
}
