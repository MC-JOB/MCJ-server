package com.minecraft.job.api.service;

import com.minecraft.job.api.service.dto.ReviewCreateDto;
import com.minecraft.job.api.service.dto.ReviewUpdateDto;
import com.minecraft.job.common.review.domain.Review;
import org.springframework.data.util.Pair;

public interface ReviewAppService {

    Pair<Review, Long> create(ReviewCreateDto dto);

    void update(ReviewUpdateDto dto);
}
