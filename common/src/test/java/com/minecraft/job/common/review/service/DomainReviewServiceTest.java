package com.minecraft.job.common.review.service;

import com.minecraft.job.common.fixture.ReviewFixture;
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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

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
    private User leader;
    private Team team;
    private Review review;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());

        leader = userRepository.save(UserFixture.getAntherUser("leader"));
        team = teamRepository.save(TeamFixture.create(leader));

        review = reviewRepository.save(ReviewFixture.create(user, team));
    }

    @Test
    void 리뷰_생성_성공() {
        Review findReview = reviewRepository.findById(review.getId()).orElseThrow();

        assertThat(findReview.getId()).isNotNull();
    }

    @Test
    void 리뷰_수정_성공() {
        reviewService.update(review.getId(), user.getId(), team.getId(), "updateContent", 1L);

        Review findReview = reviewRepository.findById(review.getId()).orElseThrow();

        assertThat(findReview.getContent()).isEqualTo("updateContent");
        assertThat(findReview.getScore()).isEqualTo(1L);
    }

    @Test
    void 리뷰_수정_실패__유저의_리뷰가_아님() {
        User fakerUser = userRepository.save(UserFixture.getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(() -> reviewService.update(review.getId(), fakerUser.getId(), team.getId(), "updateContent", 1L));
    }

    @Test
    void 리뷰_수정_실패__팀의_리뷰가_아님() {
        Team fakeTeam = teamRepository.save(TeamFixture.getFakeTeam(leader));

        assertThatIllegalArgumentException().isThrownBy(() -> reviewService.update(review.getId(), user.getId(), fakeTeam.getId(), "updateContent", 1L));
    }
}
