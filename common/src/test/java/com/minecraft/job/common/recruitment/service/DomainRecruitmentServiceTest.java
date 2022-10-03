package com.minecraft.job.common.recruitment.service;

import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.domain.RecruitmentRepository;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.minecraft.job.common.recruitment.domain.RecruitmentStatus.ACTIVATED;
import static com.minecraft.job.common.recruitment.domain.RecruitmentStatus.INACTIVATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
@Transactional
class DomainRecruitmentServiceTest {

    @Autowired
    private RecruitmentService recruitmentService;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    private User user;

    private Team team;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());
        team = teamRepository.save(TeamFixture.create(user));
    }

    @Test
    void 채용공고_생성_성공() {
        Recruitment recruitment = recruitmentService.create(user.getId(), team.getId(), "title", "content");

        Recruitment findRecruitment = recruitmentRepository.findById(recruitment.getId()).orElseThrow();

        assertThat(findRecruitment.getId()).isNotNull();
    }

    @Test
    void 채용공고_생성_실패__유저의_팀이_아님() {
        User fakeUser = userRepository.save(UserFixture.getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(
                () -> recruitmentService.create(fakeUser.getId(), team.getId(), "title", "content")
        );
    }

    @Test
    void 채용공고_활성화_성공() {
        Recruitment recruitment = recruitmentService.create(user.getId(), team.getId(), "title", "content");

        recruitmentService.activate(recruitment.getId(), user.getId(), team.getId(), LocalDateTime.now().plusMinutes(1));

        Recruitment findRecruitment = recruitmentRepository.findById(recruitment.getId()).orElseThrow();

        assertThat(findRecruitment.getStatus()).isEqualTo(ACTIVATED);
        assertThat(findRecruitment.getClosedAt()).isNotNull();
    }

    @Test
    void 채용공고_활성화_실패__유저의_팀이_아님() {
        Recruitment recruitment = recruitmentService.create(user.getId(), team.getId(), "title", "content");

        User fakeUser = userRepository.save(UserFixture.getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(
                () -> recruitmentService.activate(recruitment.getId(), fakeUser.getId(), team.getId(), LocalDateTime.now().plusMinutes(1))
        );
    }

    @Test
    void 채용공고_활성화_실패__팀의_채용공고가_아님() {
        Recruitment recruitment = recruitmentService.create(user.getId(), team.getId(), "title", "content");

        Team fakeTeam = teamRepository.save(TeamFixture.getFakeTeam(user));

        assertThatIllegalArgumentException().isThrownBy(
                () -> recruitmentService.activate(recruitment.getId(), user.getId(), fakeTeam.getId(), LocalDateTime.now().plusMinutes(1))
        );
    }

    @Test
    void 채용공고_비활성화_성공() {
        Recruitment recruitment = recruitmentService.create(user.getId(), team.getId(), "title", "content");

        recruitmentService.activate(recruitment.getId(), user.getId(), team.getId(), LocalDateTime.now().plusMinutes(1));

        recruitmentService.inactivate(recruitment.getId(), user.getId(), team.getId());

        Recruitment findRecruitment = recruitmentRepository.findById(recruitment.getId()).orElseThrow();

        assertThat(findRecruitment.getStatus()).isEqualTo(INACTIVATED);
        assertThat(findRecruitment.getClosedAt()).isNull();
    }
}
