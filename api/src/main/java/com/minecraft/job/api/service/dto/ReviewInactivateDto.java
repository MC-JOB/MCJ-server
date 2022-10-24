package com.minecraft.job.api.service.dto;

public record ReviewInactivateDto(
        Long reviewId,
        Long userId,
        Long teamId
) {
}
