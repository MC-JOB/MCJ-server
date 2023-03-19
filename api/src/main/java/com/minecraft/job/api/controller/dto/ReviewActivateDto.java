package com.minecraft.job.api.controller.dto;

public class ReviewActivateDto {

    public record ReviewActivateRequest(
            Long reviewId, Long teamId, String content, Long score
    ) {

        public com.minecraft.job.api.service.dto.ReviewActivateDto toDto(Long userId) {
            return new com.minecraft.job.api.service.dto.ReviewActivateDto(reviewId, userId, teamId, content, score);
        }
    }
}
