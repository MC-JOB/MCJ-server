package com.minecraft.job.common.review.service;

import com.minecraft.job.common.review.domain.*;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.minecraft.job.common.support.Preconditions.require;

@Service
@Transactional
@RequiredArgsConstructor
public class DomainReviewService implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Review create(Long userId, Long teamId, String content, Long score) {
        User user = userRepository.findById(userId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();

        Review review = Review.create(content, score, user, team);

        review = reviewRepository.save(review);

        eventPublisher.publishEvent(new ReviewCreateEvent(review.getId()));

        return review;
    }

    @Override
    public void update(Long reviewId, Long userId, Long teamId, String content, Long score) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();

        require(review.ofUser(user));
        require(review.ofTeam(team));

        review.update(content, score);
    }

    @Override
    public void active(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();

        review.activate();
    }

    @Override
    public void inactive(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();

        review.inactivate();
    }

    @Override
    public Page<Review> getMyReviews(ReviewSearchType searchType, String searchName, Pageable pageable, User user) {

        Specification<Review> spec = getReviewSpecification(searchType, searchName)
                .and(ReviewSpecification.equalUser(user));

        return reviewRepository.findAll(spec, pageable);
    }

    private Specification<Review> getReviewSpecification(ReviewSearchType searchType, String searchName) {
        Specification<Review> spec = null;

        if (searchType == ReviewSearchType.CONTENT) {
            spec = Specification.where(ReviewSpecification.likeContent(searchName));
        }
        if (searchType == ReviewSearchType.TEAM) {
            Team team = teamRepository.findByName(searchName);
            spec = Specification.where(ReviewSpecification.equalTeam(team));
        }
        return spec;
    }
}
