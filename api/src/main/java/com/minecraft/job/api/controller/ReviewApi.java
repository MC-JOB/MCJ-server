package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.ReviewGetListDto.ReviewGetListResponse;
import com.minecraft.job.api.controller.dto.review.ReviewActivateDto.ReviewActivateRequest;
import com.minecraft.job.api.controller.dto.review.ReviewCreateDto.ReviewCreateRequest;
import com.minecraft.job.api.controller.dto.review.ReviewCreateDto.ReviewCreateResponse;
import com.minecraft.job.api.controller.dto.review.ReviewInactivateDto.ReviewInactivateRequest;
import com.minecraft.job.api.controller.dto.review.ReviewUpdateDto.ReviewUpdateRequest;
import com.minecraft.job.api.security.user.DefaultMcjUser;
import com.minecraft.job.api.service.ReviewAppService;
import com.minecraft.job.common.review.domain.Review;
import com.minecraft.job.common.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.minecraft.job.api.controller.dto.ReviewGetListDto.ReviewGetListData;
import static com.minecraft.job.api.controller.dto.ReviewGetListDto.ReviewGetListRequest;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewApi {

    private final ReviewService reviewService;
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

    @GetMapping("/getMyReviewList")
    public ReviewGetListResponse getMyReviewList(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody ReviewGetListRequest req
    ) {
        PageRequest pageable = PageRequest.of(req.page(), req.size());

        Page<Review> reviewList = reviewService.getMyReviewList(req.searchType(), req.searchName(), pageable, user.getId());

        return ReviewGetListResponse.getReviewList(ReviewGetListData.getReviewList(reviewList));
    }
}
