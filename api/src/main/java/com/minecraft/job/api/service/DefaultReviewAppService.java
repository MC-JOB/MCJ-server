package com.minecraft.job.api.service;

import com.minecraft.job.api.service.dto.ReviewCreateDto;
import com.minecraft.job.common.review.domain.Review;
import com.minecraft.job.common.review.domain.ReviewRepository;
import com.minecraft.job.common.review.service.ReviewService;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.LongStream;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultReviewAppService implements ReviewAppService {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final TeamService teamService;
    private final TeamRepository teamRepository;

    @Override
    public Pair<Review, Long> create(ReviewCreateDto dto) {
        Team team = teamRepository.findById(dto.teamId()).orElseThrow();

        Review review = reviewService.create(dto.userId(), dto.teamId(), dto.content(), dto.score());

        List<Review> reviews = reviewRepository.findReviewByTeam(team);

        Long averagePoint = Double.valueOf(
                        reviews.stream()
                                .flatMapToLong(it -> LongStream.of(it.getScore()))
                                .average()
                                .orElseThrow())
                .longValue();

        teamService.applyAveragePoint(dto.teamId(), averagePoint);

        return Pair.of(review, averagePoint);
    }
}
