package com.minecraft.job.common.teamlogo.domain;

import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.TeamLogoFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.*;


class TeamLogoTest {

    private Team team;

    @BeforeEach
    void setUp() {
        User user = UserFixture.create();
        team = TeamFixture.create(user);
    }

    @Test
    void 팀로고_생성_성공() {
        TeamLogo teamLogo = TeamLogo.create("name", "savedName", 1L, team);

        assertThat(teamLogo.getName()).isEqualTo("name");
        assertThat(teamLogo.getSavedName()).isEqualTo("savedName");
        assertThat(teamLogo.getSize()).isEqualTo(1L);
        assertThat(teamLogo.getTeam()).isSameAs(team);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 팀로고_생성_실패__이름이_널_혹은_공백(String name) {
        assertThatIllegalArgumentException().isThrownBy(() -> TeamLogo.create(name, "savedName", 1L, team));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 팀로고_생성_실패__저장_이름이_널_혹은_공백(String savedName) {
        assertThatIllegalArgumentException().isThrownBy(() -> TeamLogo.create("name", savedName, 1L, team));
    }

    @Test
    void 팀로고_생성_실패_사이즈가_0이하() {
        assertThatIllegalArgumentException().isThrownBy(() -> TeamLogo.create("name", "savedName", 0L, team));
    }

    @Test
    void 팀로고_생성_실패_팀이_널() {
        assertThatNullPointerException().isThrownBy(() -> TeamLogo.create("name", "savedName", 1L, null));
    }

    @Test
    void 팀로고_수정_성공() {
        TeamLogo teamLogo = TeamLogoFixture.create(team);

        teamLogo.update("updateName", "UpdateSavedName", 100L);

        assertThat(teamLogo.getName()).isEqualTo("updateName");
        assertThat(teamLogo.getSavedName()).isEqualTo("UpdateSavedName");
        assertThat(teamLogo.getSize()).isEqualTo(100L);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 팀로고_수정_실패__이름이_널_혹은_공백(String name) {
        TeamLogo teamLogo = TeamLogoFixture.create(team);

        assertThatIllegalArgumentException().isThrownBy(() -> teamLogo.update(name, "UpdateSavedName", 100L));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 팀로고_수정_실패__저장_이름이_널_혹은_공백(String savedName) {
        TeamLogo teamLogo = TeamLogoFixture.create(team);

        assertThatIllegalArgumentException().isThrownBy(() -> teamLogo.update("updateName", savedName, 100L));
    }

    @Test
    void 팀로고_수정_실패_사이즈가_0이하() {
        TeamLogo teamLogo = TeamLogoFixture.create(team);

        assertThatIllegalArgumentException().isThrownBy(() -> teamLogo.update("updateName", "UpdateSavedName", 0L));
    }
}
