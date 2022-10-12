package com.minecraft.job.api.service.dto;

public record ReviewCreateDto(
        Long userId,
        Long teamId,
        String content,
        Long score
) {
}
