package com.minecraft.job.api.service.dto;

public record ReviewActivateDto(
        Long reviewId,
        Long userId,
        Long teamId,
        String content,
        Long score
) {
}
