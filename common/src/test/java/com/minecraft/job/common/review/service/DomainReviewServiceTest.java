package com.minecraft.job.common.review.service;

import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.review.domain.Review;
import com.minecraft.job.common.review.domain.ReviewRepository;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.RecordApplicationEvents;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@RecordApplicationEvents
class DomainReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    private User user;
    private Team team;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());

        User leader = userRepository.save(UserFixture.getAntherUser("leader"));
        team = teamRepository.save(TeamFixture.create(leader));
    }

    @Test
    void 리뷰_생성_성공() {
        Review review = reviewService.create(user.getId(), team.getId(), "content", 3L);

        Review findReview = reviewRepository.findById(review.getId()).orElseThrow();

        assertThat(findReview.getId()).isNotNull();
    }
}
