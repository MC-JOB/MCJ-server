package com.minecraft.job.api.controller.dto.user;

public class UserChangePasswordDto {

    public record UserChangePasswordRequest(
            String password, String newPassword
    ) {
    }
}
