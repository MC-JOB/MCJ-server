package com.minecraft.job.api.service;

import com.minecraft.job.api.service.dto.ReviewActivateDto;
import com.minecraft.job.api.service.dto.ReviewCreateDto;
import com.minecraft.job.api.service.dto.ReviewInactivateDto;
import com.minecraft.job.api.service.dto.ReviewUpdateDto;
import com.minecraft.job.common.review.domain.Review;
import com.minecraft.job.common.review.domain.ReviewCreateEvent;
import com.minecraft.job.common.review.domain.ReviewRepository;
import com.minecraft.job.common.review.service.ReviewService;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.team.service.TeamService;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import com.minecraft.job.integration.mail.Mail;
import com.minecraft.job.integration.mail.MailApi;
import com.minecraft.job.integration.mail.MailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

import static com.minecraft.job.common.support.Preconditions.require;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultReviewAppService implements ReviewAppService {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final TeamService teamService;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final MailApi mailApi;

    @Override
    public Pair<Review, Double> create(ReviewCreateDto dto) {
        Review review = reviewService.create(dto.userId(), dto.teamId(), dto.content(), dto.score());

        double averagePoint = setAveragePoint(dto.teamId());

        return Pair.of(review, averagePoint);
    }

    @Override
    public void update(ReviewUpdateDto dto) {
        reviewService.update(dto.reviewId(), dto.userId(), dto.teamId(), dto.content(), dto.score());

        setAveragePoint(dto.teamId());
    }

    @Override
    public void activate(ReviewActivateDto dto) {
        reviewService.active(dto.reviewId());

        reviewService.update(dto.reviewId(), dto.userId(), dto.teamId(), dto.content(), dto.score());

        setAveragePoint(dto.teamId());
    }

    @Override
    public void inactivate(ReviewInactivateDto dto) {
        Review review = reviewRepository.findById(dto.reviewId()).orElseThrow();
        User user = userRepository.findById(dto.userId()).orElseThrow();
        Team team = teamRepository.findById(dto.teamId()).orElseThrow();

        require(review.ofUser(user));
        require(review.ofTeam(team));

        reviewService.inactive(dto.reviewId());

        setAveragePoint(dto.teamId());
    }

    private double setAveragePoint(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow();

        List<Review> reviews = reviewRepository.findAllActivated(team);

        double averagePoint = reviews.stream()
                .flatMapToLong(it -> LongStream.of(it.getScore()))
                .average()
                .orElseThrow();

        averagePoint = Math.round(averagePoint * 10) / 10.0;

        teamService.applyAveragePoint(teamId, averagePoint);

        return averagePoint;
    }

    @Async
    @TransactionalEventListener(ReviewCreateEvent.class)
    public void onCreateReviewListener(ReviewCreateEvent event) throws Exception {
        Review review = reviewRepository.findById(event.reviewId()).orElseThrow();

        String leaderEmail = review.getTeamOfLeaderEmail();
        String teamName = review.getTeamName();
        String userNickname = review.getUserNickname();
        Long score = review.getScore();
        Double averagePoint = review.getTeamOfAveragePoint();

        mailApi.send(new Mail(
                new String[]{leaderEmail},
                MailTemplate.REVIEW_CREATE,
                Map.of("teamName", teamName, "userNickname", userNickname,
                        "score", score, "averagePoint", averagePoint)
        ));
    }
}
