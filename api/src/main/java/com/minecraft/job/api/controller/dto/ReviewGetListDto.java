package com.minecraft.job.api.controller.dto;

import com.minecraft.job.common.review.domain.Review;
import com.minecraft.job.common.review.domain.ReviewSearchType;
import org.springframework.data.domain.Page;

public class ReviewGetListDto {

    public record ReviewGetListRequest(
            ReviewSearchType searchType,
            String searchName,
            int page,
            int size
    ) {
    }

    public record ReviewGetListResponse(ReviewGetListData reviewList) {

        public static ReviewGetListResponse getReviewList(ReviewGetListData reviewGetListData) {
            return new ReviewGetListResponse(reviewGetListData);
        }

    }

    public record ReviewGetListData(Page<Review> reviewList) {

        public static ReviewGetListData getReviewList(Page<Review> reviewList) {

            return new ReviewGetListData(reviewList);
        }
    }
}
