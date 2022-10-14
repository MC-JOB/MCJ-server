package com.minecraft.job.common.recruitmentProcess.domain;

import com.minecraft.job.common.fixture.RecruitmentFixture;
import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessStatus.IN_PROGRESS;
import static com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessStatus.WAITING;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;

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

    @Test
    void 채용과정_서류합격_성공() {
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user);

        recruitmentProcess.inProgress();

        assertThat(recruitmentProcess.getStatus()).isEqualTo(IN_PROGRESS);
    }

    @ParameterizedTest
    @EnumSource(value = RecruitmentProcessStatus.class, names = {"IN_PROGRESS", "PASSED", "CANCELED", "FAILED"}, mode = INCLUDE)
    void 채용과정_서류합격_실패__서류합격_가능한_상태아님(RecruitmentProcessStatus status) {
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user);

        recruitmentProcess.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> recruitmentProcess.inProgress());
    }
}
