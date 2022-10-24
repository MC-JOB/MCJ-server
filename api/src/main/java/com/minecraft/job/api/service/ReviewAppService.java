package com.minecraft.job.api.service;

import com.minecraft.job.api.service.dto.ReviewActivateDto;
import com.minecraft.job.api.service.dto.ReviewCreateDto;
import com.minecraft.job.api.service.dto.ReviewInactivateDto;
import com.minecraft.job.api.service.dto.ReviewUpdateDto;
import com.minecraft.job.common.review.domain.Review;
import org.springframework.data.util.Pair;

public interface ReviewAppService {

    Pair<Review, Double> create(ReviewCreateDto dto);

    void update(ReviewUpdateDto dto);

    void activate(ReviewActivateDto dto);

    void inactivate(ReviewInactivateDto dto);
}
