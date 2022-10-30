package com.minecraft.job.common.recruitmentProcess.domain;

import com.minecraft.job.common.fixture.RecruitmentFixture;
import com.minecraft.job.common.fixture.ResumeFixture;
import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;

class RecruitmentProcessTest {

    private Recruitment recruitment;
    private User user;
    private User teamUser;
    private User fakeUser;
    private Resume resume;

    @BeforeEach
    void setUp() {
        user = UserFixture.create();
        teamUser = UserFixture.getAntherUser("teamUser");
        fakeUser = UserFixture.getFakerUser();

        Team team = TeamFixture.create(teamUser);

        recruitment = RecruitmentFixture.create(team);

        resume = ResumeFixture.create(user);
    }

    @Test
    void 채용과정_생성_성공() {
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user, resume);

        assertThat(recruitmentProcess.getRecruitment()).isEqualTo(recruitment);
        assertThat(recruitmentProcess.getUser()).isEqualTo(user);
        assertThat(recruitmentProcess.getResume()).isEqualTo(resume);
        assertThat(recruitmentProcess.getStatus()).isEqualTo(WAITING);
        assertThat(recruitmentProcess.getCreatedAt()).isNotNull();
    }

    @Test
    void 채용과정_생성_실패__채용공고가_널() {
        assertThatNullPointerException().isThrownBy(() -> RecruitmentProcess.create(null, user, resume));
    }

    @Test
    void 채용과정_생성_실패__유저가_널() {
        assertThatNullPointerException().isThrownBy(() -> RecruitmentProcess.create(recruitment, null, resume));
    }

    @Test
    void 채용과정_생성_실패__이력서가_널() {
        assertThatNullPointerException().isThrownBy(() -> RecruitmentProcess.create(recruitment, user, null));
    }

    @Test
    void 채용과정_생성_실패__채용공고의_주인임() {
        assertThatIllegalArgumentException().isThrownBy(() -> RecruitmentProcess.create(recruitment, teamUser, resume));
    }

    @Test
    void 채용과정_생성_실패__이력서의_주인이_아님() {
        assertThatIllegalArgumentException().isThrownBy(() -> RecruitmentProcess.create(recruitment, fakeUser, resume));
    }

    @Test
    void 채용과정_서류합격_성공() {
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user, resume);

        recruitmentProcess.inProgress();

        assertThat(recruitmentProcess.getStatus()).isEqualTo(IN_PROGRESS);
    }

    @ParameterizedTest
    @EnumSource(value = RecruitmentProcessStatus.class, names = {"IN_PROGRESS", "PASSED", "CANCELED", "FAILED"}, mode = INCLUDE)
    void 채용과정_서류합격_실패__서류합격_가능한_상태아님(RecruitmentProcessStatus status) {
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user, resume);

        recruitmentProcess.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> recruitmentProcess.inProgress());
    }

    @Test
    void 채용과정_중도취소_성공() {
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user, resume);

        recruitmentProcess.cancel();

        assertThat(recruitmentProcess.getStatus()).isEqualTo(CANCELED);
        assertThat(recruitmentProcess.getClosedAt()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(value = RecruitmentProcessStatus.class, names = {"PASSED", "CANCELED", "FAILED"}, mode = INCLUDE)
    void 채용과정_중도취소_실패__중도취소_가능한_상태아님(RecruitmentProcessStatus status) {
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user, resume);

        recruitmentProcess.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> recruitmentProcess.cancel());
    }

    @Test
    void 채용과정_최종합격_성공() {
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user, resume);

        recruitmentProcess.inProgress();

        recruitmentProcess.pass();

        assertThat(recruitmentProcess.getStatus()).isEqualTo(PASSED);
        assertThat(recruitmentProcess.getClosedAt()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(value = RecruitmentProcessStatus.class, names = {"WAITING", "PASSED", "CANCELED", "FAILED"}, mode = INCLUDE)
    void 채용과정_최종합격_실패__최종합격_가능한_상태아님(RecruitmentProcessStatus status) {
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user, resume);

        recruitmentProcess.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> recruitmentProcess.pass());
    }

    @Test
    void 채용과정_불합격_성공() {
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user, resume);

        recruitmentProcess.fail();

        assertThat(recruitmentProcess.getStatus()).isEqualTo(FAILED);
        assertThat(recruitmentProcess.getClosedAt()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(value = RecruitmentProcessStatus.class, names = {"PASSED", "CANCELED", "FAILED"}, mode = INCLUDE)
    void 채용과정_불합격_실패__불합격_가능한_상태아님(RecruitmentProcessStatus status) {
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user, resume);

        recruitmentProcess.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> recruitmentProcess.fail());
    }
}
