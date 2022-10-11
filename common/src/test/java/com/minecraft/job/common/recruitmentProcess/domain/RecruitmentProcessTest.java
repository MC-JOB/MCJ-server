package com.minecraft.job.common.recruitmentProcess.domain;

import com.minecraft.job.common.fixture.RecruitmentFixture;
import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessStatus.WAITING;
import static org.assertj.core.api.Assertions.*;

class RecruitmentProcessTest {

    private Recruitment recruitment;
    private User user;
    private User teamUser;

    @BeforeEach
    void setUp() {
        user = UserFixture.create();

        teamUser = UserFixture.getAntherUser("teamUser");
        Team team = TeamFixture.create(teamUser);
        recruitment = RecruitmentFixture.create(team);
    }

    @Test
    void 채용과정_생성_성공() {
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user);

        assertThat(recruitmentProcess.getRecruitment()).isEqualTo(recruitment);
        assertThat(recruitmentProcess.getUser()).isEqualTo(user);
        assertThat(recruitmentProcess.getStatus()).isEqualTo(WAITING);
        assertThat(recruitmentProcess.getCreatedAt()).isNotNull();
    }

    @Test
    void 채용과정_생성_실패__채용공고가_널() {
        assertThatNullPointerException().isThrownBy(() -> RecruitmentProcess.create(null, user));
    }

    @Test
    void 채용과정_생성_실패__유저가_널() {
        assertThatNullPointerException().isThrownBy(() -> RecruitmentProcess.create(recruitment, null));
    }

    @Test
    void 채용과정_생성_실패__채용공고의_주인임() {
        assertThatIllegalArgumentException().isThrownBy(() -> RecruitmentProcess.create(recruitment, teamUser));
    }
}
