package com.minecraft.job.common.review.domain;

import com.minecraft.job.common.fixture.ReviewFixture;
import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.minecraft.job.common.review.domain.Review.MAX_SCORE;
import static com.minecraft.job.common.review.domain.Review.MIN_SCORE;
import static com.minecraft.job.common.review.domain.ReviewStatus.ACTIVATED;
import static com.minecraft.job.common.review.domain.ReviewStatus.INACTIVATED;
import static org.assertj.core.api.Assertions.*;


class ReviewTest {

    private User user;
    private Team team;
    private Review review;

    @BeforeEach
    void setUp() {
        user = UserFixture.create();
        User teamUser = UserFixture.getAntherUser("teamUser");
        team = TeamFixture.create(teamUser);
        review = ReviewFixture.create(user, team);
    }

    @Test
    void 리뷰_생성_성공() {
        Review review = Review.create("content", 3L, user, team);

        assertThat(review.getContent()).isEqualTo("content");
        assertThat(review.getScore()).isEqualTo(3L);
        assertThat(review.getUser()).isEqualTo(user);
        assertThat(review.getTeam()).isEqualTo(team);
        assertThat(review.getStatus()).isEqualTo(ACTIVATED);
        assertThat(review.getCreatedAt()).isNotNull();
    }

    @Test
    void 리뷰_생성_실패__유저가_널() {
        assertThatNullPointerException().isThrownBy(() -> Review.create("content", 10L, null, team));
    }

    @Test
    void 리뷰_생성_실패__팀이_널() {
        assertThatNullPointerException().isThrownBy(() -> Review.create("content", 10L, user, null));
    }

    @Test
    void 리뷰_생성_실패__유저가_팀의_소유() {
        User user = UserFixture.create();
        Team team = TeamFixture.create(user);

        assertThatIllegalArgumentException().isThrownBy(
                () -> Review.create("content", 3L, user, team)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 리뷰_생성_실패__본문이_널이거나_공백(String content) {
        assertThatIllegalArgumentException().isThrownBy(() -> Review.create(content, 10L, user, team));
    }

    @Test
    void 리뷰_생성_실패__최소값보다_낮음() {
        assertThatIllegalArgumentException().isThrownBy(() -> Review.create("content", MIN_SCORE - 1, user, team));
    }

    @Test
    void 리뷰_생성_실패__최대값보다_높음() {
        assertThatIllegalArgumentException().isThrownBy(() -> Review.create("content", MAX_SCORE + 1, user, team));
    }

    @Test
    void 리뷰_활성화_성공() {
        review.setStatus(INACTIVATED);

        review.activate();

        assertThat(review.getStatus()).isEqualTo(ACTIVATED);
    }

    @Test
    void 리뷰_활성화_실패__이미_활성화_상태() {
        review.setStatus(ACTIVATED);

        assertThatIllegalStateException().isThrownBy(() -> review.activate());
    }
}
