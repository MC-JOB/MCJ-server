package com.minecraft.job.common.team.domain;

import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.minecraft.job.common.team.domain.Team.MAX_AVERAGE_POINT;
import static com.minecraft.job.common.team.domain.Team.MIN_AVERAGE_POINT;
import static com.minecraft.job.common.team.domain.TeamStatus.ACTIVATED;
import static com.minecraft.job.common.team.domain.TeamStatus.INACTIVATED;
import static org.assertj.core.api.Assertions.*;

class TeamTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = UserFixture.create();
    }

    @Test
    void 팀_생성_성공() {
        Team team = Team.create("name", "description", "logo", 5L, user);

        assertThat(team.getName()).isEqualTo("name");
        assertThat(team.getDescription()).isEqualTo("description");
        assertThat(team.getLogo()).isEqualTo("logo");
        assertThat(team.getMemberNum()).isEqualTo(5L);
        assertThat(team.getUser()).isEqualTo(user);
        assertThat(team.getStatus()).isEqualTo(ACTIVATED);
        assertThat(team.getAveragePoint()).isEqualTo(0L);
        assertThat(team.getCreatedAt()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 팀_생성_실패__name이_널이거나_공백(String name) {
        assertThatIllegalArgumentException().isThrownBy(() -> Team.create(name, "description", "logo", 5L, user));
    }

    @Test
    void 팀_생성_실패__memberNum이_음수() {
        assertThatIllegalArgumentException().isThrownBy(() -> Team.create("name", "description", "logo", -1L, user));
    }

    @Test
    void 팀_평점_적용_성공() {
        Team team = TeamFixture.create(user);

        team.applyAveragePoint(5L);

        assertThat(team.getAveragePoint()).isEqualTo(5L);
    }

    @Test
    void 팀_평점_적용_실패__평점_최소값보다_낮음() {
        Team team = TeamFixture.create(user);

        assertThatIllegalArgumentException().isThrownBy(() -> team.applyAveragePoint(MIN_AVERAGE_POINT - 1L));
    }

    @Test
    void 팀_평점_적용_실패__평점_최대값보다_높음() {
        Team team = TeamFixture.create(user);

        assertThatIllegalArgumentException().isThrownBy(() -> team.applyAveragePoint(MAX_AVERAGE_POINT + 1L));
    }

    @Test
    void 팀_업데이트_성공() {
        Team team = TeamFixture.create(user);

        team.update("updateName", "updateDescription", "updateLogo", 1L);

        assertThat(team.getName()).isEqualTo("updateName");
        assertThat(team.getDescription()).isEqualTo("updateDescription");
        assertThat(team.getLogo()).isEqualTo("updateLogo");
        assertThat(team.getMemberNum()).isEqualTo(1L);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 팀_업데이트_실패__name이_널이거나_공백(String name) {
        Team team = TeamFixture.create(user);

        assertThatIllegalArgumentException().isThrownBy(() -> team.update(name, "updateDescription", "updateLogo", 1L));
    }

    @Test
    void 팀_업데이트_실패__memberNum이_음수() {
        Team team = TeamFixture.create(user);

        assertThatIllegalArgumentException().isThrownBy(() -> team.update("updateName", "updateDescription", "updateLogo", -1L));
    }

    @Test
    void 팀_업데이트_실패__활성화_상태가_아님() {
        Team team = TeamFixture.create(user);

        team.inactivate();

        assertThatIllegalStateException().isThrownBy(() -> team.update("updateName", "updateDescription", "updateLogo", 1L));
    }

    @Test
    void 팀_비활성화_성공() {
        Team team = TeamFixture.create(user);

        team.inactivate();

        assertThat(team.getStatus()).isEqualTo(INACTIVATED);
    }

    @Test
    void 팀_비활성화_실패__이미_비활성화_상태() {
        Team team = TeamFixture.create(user);

        team.inactivate();

        assertThatIllegalStateException().isThrownBy(team::inactivate);
    }
}
