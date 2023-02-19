package com.minecraft.job.common.review.domain;

import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static com.minecraft.job.common.review.domain.ReviewStatus.ACTIVATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class ReviewRepositoryTest {

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
        User teamUser = userRepository.save(UserFixture.getAntherUser("teamUser"));
        team = teamRepository.save(TeamFixture.create(teamUser));
    }

    @Test
    void 리뷰_생성_성공() {
        Review review = Review.create("content", 3L, user, team);

        review = reviewRepository.save(review);

        Review findReview = reviewRepository.findById(review.getId()).orElseThrow();

        assertThat(findReview.getId()).isNotNull();
        assertThat(findReview.getContent()).isEqualTo("content");
        assertThat(findReview.getScore()).isEqualTo(3L);
        assertThat(findReview.getUser()).isEqualTo(user);
        assertThat(findReview.getTeam()).isEqualTo(team);
        assertThat(findReview.getStatus()).isEqualTo(ACTIVATED);
    }

    @Test
    void 리뷰_팀으로_활성화_상태_조회() {
        User user1 = userRepository.save(UserFixture.getAntherUser("user1"));
        User user2 = userRepository.save(UserFixture.getAntherUser("user2"));
        User user3 = userRepository.save(UserFixture.getAntherUser("user3"));

        Review review1 = reviewRepository.save(Review.create("content", 4L, user1, team));
        Review review2 = reviewRepository.save(Review.create("content", 3L, user2, team));
        Review review3 = reviewRepository.save(Review.create("content", 3L, user3, team));

        review3.inactivate();

        List<Review> findReviews = reviewRepository.findAllActivated(team);

        assertThat(findReviews).containsExactly(review1, review2);
        assertThat(findReviews).doesNotContain(review3);
    }

    @Test
    void 리뷰_조회_성공_유저__아이디가_일치하는_경우() {
        Review review = Review.create("content", 3L, user, team);

        reviewRepository.save(review);

        Optional<Review> findReview = reviewRepository.findByUser_Id(user.getId());

        assertThat(findReview).isNotNull();
        assertThat(findReview.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void 리뷰_조회_실패__유저_아이디가_일치하지_않는_경우() {
        Review review = Review.create("content", 3L, user, team);

        reviewRepository.save(review);

        Optional<Review> findReview = reviewRepository.findByUser_Id(2L);

        assertThat(findReview).isEmpty();
    }
}
