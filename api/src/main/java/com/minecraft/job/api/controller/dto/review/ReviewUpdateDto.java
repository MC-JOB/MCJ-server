package com.minecraft.job.api.controller.dto.review;

public class ReviewUpdateDto {

    public record ReviewUpdateRequest(
            Long reviewId, Long teamId, String content, Long score
    ) {

        public com.minecraft.job.api.service.dto.ReviewUpdateDto toDto(Long userId) {
            return new com.minecraft.job.api.service.dto.ReviewUpdateDto(reviewId, userId, teamId, content, score);
        }
    }
}
