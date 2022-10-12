package com.minecraft.job.api.service;

import com.minecraft.job.api.fixture.TeamFixture;
import com.minecraft.job.api.fixture.UserFixture;
import com.minecraft.job.api.service.dto.ReviewCreateDto;
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

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class DefaultReviewAppServiceTest {

    @Autowired
    private ReviewAppService reviewAppService;

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
    void 리뷰_생성_성공__평점_적용() {
        User user1 = userRepository.save(UserFixture.getAntherUser("user1"));
        reviewRepository.save(Review.create("content", 3L, user1, team));

        User user2 = userRepository.save(UserFixture.getAntherUser("user2"));
        reviewRepository.save(Review.create("content", 4L, user2, team));

        ReviewCreateDto content = new ReviewCreateDto(user.getId(), team.getId(), "content", 5L);
        Review review = reviewAppService.create(content).getFirst();

        Review findReview = reviewRepository.findById(review.getId()).orElseThrow();
        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findReview.getId()).isNotNull();
        assertThat(findTeam.getAveragePoint()).isEqualTo((3 + 4 + 5) / 3);
    }
}
