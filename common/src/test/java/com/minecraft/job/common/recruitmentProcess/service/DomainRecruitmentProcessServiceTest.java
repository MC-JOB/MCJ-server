package com.minecraft.job.common.recruitmentProcess.service;

import com.minecraft.job.common.fixture.RecruitmentFixture;
import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.domain.RecruitmentRepository;
import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcess;
import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessCreateEvent;
import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessRepository;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

import static com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Transactional
@SpringBootTest
@RecordApplicationEvents
public class DomainRecruitmentProcessServiceTest {

    @Autowired
    private ApplicationEvents applicationEvents;

    @Autowired
    private RecruitmentProcessService recruitmentProcessService;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RecruitmentProcessRepository recruitmentProcessRepository;

    private Recruitment recruitment;
    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());

        User leader = userRepository.save(UserFixture.getAntherUser("leader"));
        Team team = teamRepository.save(TeamFixture.create(leader));

        recruitment = recruitmentRepository.save(RecruitmentFixture.create(team));
    }

    @Test
    void 채용과정_생성_성공() {
        RecruitmentProcess recruitmentProcess = recruitmentProcessService.create(recruitment.getId(), user.getId());

        RecruitmentProcess findRecruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcess.getId()).orElseThrow();

        assertThat(findRecruitmentProcess.getId()).isNotNull();
        assertThat(applicationEvents.stream(RecruitmentProcessCreateEvent.class).toList().get(0).recruitmentProcessId())
                .isEqualTo(recruitmentProcess.getId());
    }

    @Test
    void 채용과정_서류합격_성공() {
        RecruitmentProcess recruitmentProcess = recruitmentProcessService.create(recruitment.getId(), user.getId());

        recruitmentProcess.inProgress();

        RecruitmentProcess findRecruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcess.getId()).orElseThrow();

        assertThat(findRecruitmentProcess.getStatus()).isEqualTo(IN_PROGRESS);
    }

    @Test
    void 채용과정_서류합격_실패__팀의_채용과정이_아님() {
        RecruitmentProcess recruitmentProcess = recruitmentProcessService.create(recruitment.getId(), user.getId());

        Team fakeTeam = teamRepository.save(TeamFixture.getFakeTeam(user));

        assertThatIllegalArgumentException().isThrownBy(
                () -> recruitmentProcessService.inProgress(recruitmentProcess.getId(), fakeTeam.getId())
        );
    }

    @Test
    void 채용과정_중도취소_성공() {
        RecruitmentProcess recruitmentProcess = recruitmentProcessService.create(recruitment.getId(), user.getId());

        recruitmentProcess.cancel();

        RecruitmentProcess findRecruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcess.getId()).orElseThrow();

        assertThat(findRecruitmentProcess.getStatus()).isEqualTo(CANCELED);
        assertThat(findRecruitmentProcess.getClosedAt()).isNotNull();
    }

    @Test
    void 채용과정_중도취소_실패__팀의_채용과정이_아님() {
        RecruitmentProcess recruitmentProcess = recruitmentProcessService.create(recruitment.getId(), user.getId());

        Team fakeTeam = teamRepository.save(TeamFixture.getFakeTeam(user));

        assertThatIllegalArgumentException().isThrownBy(
                () -> recruitmentProcessService.inProgress(recruitmentProcess.getId(), fakeTeam.getId())
        );
    }

    @Test
    void 채용과정_불합격_성공() {
        RecruitmentProcess recruitmentProcess = recruitmentProcessService.create(recruitment.getId(), user.getId());

        recruitmentProcess.fail();

        RecruitmentProcess findRecruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcess.getId()).orElseThrow();

        assertThat(findRecruitmentProcess.getStatus()).isEqualTo(FAILED);
        assertThat(findRecruitmentProcess.getClosedAt()).isNotNull();
    }

    @Test
    void 채용과정_불합격_실패__팀의_채용과정이_아님() {
        RecruitmentProcess recruitmentProcess = recruitmentProcessService.create(recruitment.getId(), user.getId());

        Team fakeTeam = teamRepository.save(TeamFixture.getFakeTeam(user));

        assertThatIllegalArgumentException().isThrownBy(
                () -> recruitmentProcessService.fail(recruitmentProcess.getId(), fakeTeam.getId())
        );
    }
}
