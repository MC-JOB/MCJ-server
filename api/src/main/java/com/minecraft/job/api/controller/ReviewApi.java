package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.ReviewActivateDto.ReviewActivateRequest;
import com.minecraft.job.api.controller.dto.ReviewCreateDto.ReviewCreateRequest;
import com.minecraft.job.api.controller.dto.ReviewCreateDto.ReviewCreateResponse;
import com.minecraft.job.api.controller.dto.ReviewInactivateDto.ReviewInactivateRequest;
import com.minecraft.job.api.controller.dto.ReviewUpdateDto.ReviewUpdateRequest;
import com.minecraft.job.api.security.user.DefaultMcjUser;
import com.minecraft.job.api.service.ReviewAppService;
import com.minecraft.job.common.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ReviewCreateResponse create(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody ReviewCreateRequest req
    ) {
        Pair<Review, Double> pair = reviewAppService.create(req.toDto(user.getId()));

        return ReviewCreateResponse.create(pair);
    }

    @PostMapping("/update")
    public void update(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody ReviewUpdateRequest req
    ) {
        reviewAppService.update(req.toDto(user.getId()));
    }

    @PostMapping("/activate")
    public void activate(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody ReviewActivateRequest req
    ) {
        reviewAppService.activate(req.toDto(user.getId()));
    }

    @PostMapping("/inactivate")
    public void inactivate(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody ReviewInactivateRequest req
    ) {
        reviewAppService.inactivate(req.toDto(user.getId()));
    }
}
