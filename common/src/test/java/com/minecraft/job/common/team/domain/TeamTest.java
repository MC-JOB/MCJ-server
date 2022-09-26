package com.minecraft.job.common.team.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.minecraft.job.common.team.domain.TeamStatus.ACTIVATED;
import static com.minecraft.job.common.team.domain.TeamStatus.INACTIVATED;
import static org.assertj.core.api.Assertions.*;

class TeamTest {

    @Test
    void 팀_생성_성공() {
        Team team = Team.create("name", "email", "password", "description", "logo", 5L);

        assertThat(team.getName()).isEqualTo("name");
        assertThat(team.getEmail()).isEqualTo("email");
        assertThat(team.getPassword()).isEqualTo("password");
        assertThat(team.getDescription()).isEqualTo("description");
        assertThat(team.getLogo()).isEqualTo("logo");
        assertThat(team.getMemberNum()).isEqualTo(5L);
        assertThat(team.getTeamStatus()).isEqualTo(ACTIVATED);
        assertThat(team.getAveragePoint()).isEqualTo(0L);
        assertThat(team.getCreatedAt()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 팀_생성_실패__name이_널이거나_공백이면_실패(String name) {
        assertThatIllegalArgumentException().isThrownBy(() -> Team.create(name, "email", "password", "description", "logo", 5L));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 팀_생성_실패__email이_널이거나_공백이면_실패(String email) {
        assertThatIllegalArgumentException().isThrownBy(() -> Team.create("name", email, "password", "description", "logo", 5L));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 팀_생성_실패__password가_널이거나_공백이면_실패(String password) {
        assertThatIllegalArgumentException().isThrownBy(() -> Team.create("name", "email", password, "description", "logo", 5L));
    }

    @Test
    void 팀_생성_실패__memberNum이_음수이면_실패() {
        assertThatIllegalArgumentException().isThrownBy(() -> Team.create("name", "email", "password", "description", "logo", -1L));
    }

    @Test
    void 팀_비활성화_성공() {
        Team team = Team.create("name", "email", "password", "description", "logo", 5L);

        team.inactivate();

        assertThat(team.getTeamStatus()).isEqualTo(INACTIVATED);
    }

    @Test
    void 팀_비활성화_실패__이미_비활성화_상태() {
        Team team = Team.create("name", "email", "password", "description", "logo", 5L);

        team.inactivate();

        assertThatIllegalStateException().isThrownBy(team::inactivate);
    }
}
