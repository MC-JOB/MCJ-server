package com.minecraft.job.api.service.dto;

public record ReviewUpdateDto(
        Long reviewId,
        Long userId,
        Long teamId,
        String content,
        Long score
) {
}
