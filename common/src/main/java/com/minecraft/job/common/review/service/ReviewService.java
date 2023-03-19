package com.minecraft.job.common.review.service;

import com.minecraft.job.common.review.domain.Review;
import com.minecraft.job.common.review.domain.ReviewSearchType;
import com.minecraft.job.common.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    Review create(Long userId, Long teamId, String content, Long score);

    void update(Long reviewId, Long userId, Long teamId, String content, Long score);

    void active(Long reviewId);

    void inactive(Long reviewId);

    Page<Review> getMyReviewList(ReviewSearchType searchType, String searchName, Pageable pageable, Long userId);
}
