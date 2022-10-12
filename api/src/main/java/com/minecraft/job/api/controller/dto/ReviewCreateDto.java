package com.minecraft.job.api.controller.dto;

import com.minecraft.job.common.review.domain.Review;
import org.springframework.data.util.Pair;

public class ReviewCreateDto {

    public record ReviewCreateRequest(
            Long userId, Long teamId, String content, Long score
    ) {

        public com.minecraft.job.api.service.dto.ReviewCreateDto toDto() {
            return new com.minecraft.job.api.service.dto.ReviewCreateDto(userId, teamId, content, score);
        }
    }

    public record ReviewCreateResponse(
            ReviewCreateData review,
            Long averagePoint
    ) {
        public static ReviewCreateResponse create(Pair<Review, Long> pair) {
            return new ReviewCreateResponse(
                    ReviewCreateData.create(pair),
                    pair.getSecond()
            );
        }

    }

    public record ReviewCreateData(
            Long id,
            Long score
    ) {

        public static ReviewCreateData create(Pair<Review, Long> pair) {
            return new ReviewCreateData(pair.getFirst().getId(), pair.getFirst().getScore());
        }
    }
}
