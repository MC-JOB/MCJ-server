package com.minecraft.job.common.review.service;

import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.review.domain.Review;
import com.minecraft.job.common.review.domain.ReviewCreateEvent;
import com.minecraft.job.common.review.domain.ReviewRepository;
import com.minecraft.job.common.review.domain.ReviewSearchType;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import javax.transaction.Transactional;

import static com.minecraft.job.common.review.domain.ReviewSearchType.*;
import static com.minecraft.job.common.review.domain.ReviewStatus.ACTIVATED;
import static com.minecraft.job.common.review.domain.ReviewStatus.INACTIVATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Transactional
@SpringBootTest
@RecordApplicationEvents
class DomainReviewServiceTest {

    @Autowired
    private ApplicationEvents applicationEvents;

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

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());

        leader = userRepository.save(UserFixture.getAntherUser("leader"));
        team = teamRepository.save(TeamFixture.create(leader));
    }

    @Test
    void 리뷰_생성_성공() {
        Review review = reviewService.create(user.getId(), team.getId(), "content", 3L);

        Review findReview = reviewRepository.findById(review.getId()).orElseThrow();

        assertThat(findReview.getId()).isNotNull();
        assertThat(applicationEvents.stream(ReviewCreateEvent.class).toList().get(0).reviewId())
                .isEqualTo(review.getId());
    }

    @Test
    void 리뷰_수정_성공() {
        Review review = reviewService.create(user.getId(), team.getId(), "content", 3L);

        reviewService.update(review.getId(), user.getId(), team.getId(), "updateContent", 1L);

        Review findReview = reviewRepository.findById(review.getId()).orElseThrow();

        assertThat(findReview.getContent()).isEqualTo("updateContent");
        assertThat(findReview.getScore()).isEqualTo(1L);
    }

    @Test
    void 리뷰_수정_실패__유저의_리뷰가_아님() {
        Review review = reviewService.create(user.getId(), team.getId(), "content", 3L);

        User fakerUser = userRepository.save(UserFixture.getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(() -> reviewService.update(review.getId(), fakerUser.getId(), team.getId(), "updateContent", 1L));
    }

    @Test
    void 리뷰_수정_실패__팀의_리뷰가_아님() {
        Review review = reviewService.create(user.getId(), team.getId(), "content", 3L);

        Team fakeTeam = teamRepository.save(TeamFixture.getFakeTeam(leader));

        assertThatIllegalArgumentException().isThrownBy(() -> reviewService.update(review.getId(), user.getId(), fakeTeam.getId(), "updateContent", 1L));
    }

    @Test
    void 리뷰_활성화_성공() {
        Review review = reviewService.create(user.getId(), team.getId(), "content", 3L);

        review.inactivate();

        reviewService.active(review.getId());

        Review findReview = reviewRepository.findById(review.getId()).orElseThrow();

        assertThat(findReview.getStatus()).isEqualTo(ACTIVATED);
    }

    @Test
    void 리뷰_비활성화_성공() {
        Review review = reviewService.create(user.getId(), team.getId(), "content", 3L);

        reviewService.inactive(review.getId());

        Review findReview = reviewRepository.findById(review.getId()).orElseThrow();

        assertThat(findReview.getStatus()).isEqualTo(INACTIVATED);
    }

    @Test
    void 리뷰_리스트_조회_성공() {
        Review review = reviewService.create(user.getId(), team.getId(), "content", 3L);

        reviewRepository.findById(review.getId()).orElseThrow();

        PageRequest pageable = PageRequest.of(0, 10);

        Page<Review> myReviewList = reviewService.getMyReviews(CONTENT, "content", pageable, user);

        for (Review findReview : myReviewList) {
            assertThat(findReview.getUser()).isEqualTo(user);
        }
    }
}
