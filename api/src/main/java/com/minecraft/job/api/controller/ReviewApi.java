package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.ReviewActivateDto.ReviewActivateRequest;
import com.minecraft.job.api.controller.dto.ReviewCreateDto.ReviewCreateRequest;
import com.minecraft.job.api.controller.dto.ReviewCreateDto.ReviewCreateResponse;
import com.minecraft.job.api.controller.dto.ReviewInactivateDto.ReviewInactivateRequest;
import com.minecraft.job.api.controller.dto.ReviewUpdateDto.ReviewUpdateRequest;
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

import static com.minecraft.job.api.controller.dto.ReviewGetListDto.*;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewApi {

    private final ReviewAppService reviewAppService;
    private final ReviewService reviewService;

    @PostMapping
    public ReviewCreateResponse create(@RequestBody ReviewCreateRequest req) {
        Pair<Review, Double> pair = reviewAppService.create(req.toDto());

        return ReviewCreateResponse.create(pair);
    }

    @PostMapping("/update")
    public void update(@RequestBody ReviewUpdateRequest req) {
        reviewAppService.update(req.toDto());
    }

    @PostMapping("/activate")
    public void activate(@RequestBody ReviewActivateRequest req) {
        reviewAppService.activate(req.toDto());
    }

    @PostMapping("/inactivate")
    public void inactivate(@RequestBody ReviewInactivateRequest req) {
        reviewAppService.inactivate(req.toDto());
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
