package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.ReviewCreateDto.ReviewCreateRequest;
import com.minecraft.job.api.controller.dto.ReviewCreateDto.ReviewCreateResponse;
import com.minecraft.job.api.controller.dto.ReviewUpdateDto.ReviewUpdateRequest;
import com.minecraft.job.api.service.ReviewAppService;
import com.minecraft.job.common.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewApi {

    private final ReviewAppService reviewAppService;

    @PostMapping
    public ReviewCreateResponse create(@RequestBody ReviewCreateRequest req) {
        Pair<Review, Long> pair = reviewAppService.create(req.toDto());

        return ReviewCreateResponse.create(pair);
    }

    @PostMapping("update")
    public void update(@RequestBody ReviewUpdateRequest req) {
        reviewAppService.update(req.toDto());
    }
}
