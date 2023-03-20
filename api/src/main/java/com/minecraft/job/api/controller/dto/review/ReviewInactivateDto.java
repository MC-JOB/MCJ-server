package com.minecraft.job.api.controller.dto.review;

public class ReviewInactivateDto {

    public record ReviewInactivateRequest(
            Long reviewId, Long teamId
    ) {

        public com.minecraft.job.api.service.dto.ReviewInactivateDto toDto(Long userId) {
            return new com.minecraft.job.api.service.dto.ReviewInactivateDto(reviewId, userId, teamId);
        }
    }
}
