package com.minecraft.job.api.controller.dto;

public class ReviewUpdateDto {

    public record ReviewUpdateRequest(
            Long reviewId, Long userId, Long teamId, String content, Long score
    ) {

        public com.minecraft.job.api.service.dto.ReviewUpdateDto toDto() {
            return new com.minecraft.job.api.service.dto.ReviewUpdateDto(reviewId, userId, teamId, content, score);
        }
    }
}
