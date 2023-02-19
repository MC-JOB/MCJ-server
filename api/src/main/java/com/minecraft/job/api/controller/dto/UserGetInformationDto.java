package com.minecraft.job.api.controller.dto;

public class UserGetInformationDto {

    public record UserGetInformationRequest(
            Long userId
    ) {
    }
}
