package com.minecraft.job.api.controller.dto;

public class ReviewInactivateDto {

    public record ReviewInactivateRequest(
            Long reviewId, Long userId, Long teamId
    ) {

        public com.minecraft.job.api.service.dto.ReviewInactivateDto toDto() {
            return new com.minecraft.job.api.service.dto.ReviewInactivateDto(reviewId, userId, teamId);
        }
    }
}
