package com.minecraft.job.common.recruitmentProcess.domain;

import com.minecraft.job.common.fixture.RecruitmentFixture;
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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessStatus.WAITING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class RecruitmentProcessRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private RecruitmentProcessRepository recruitmentProcessRepository;

    private Recruitment recruitment;
    private User user;
    private User teamUser;
    private Team team;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());

        teamUser = userRepository.save(UserFixture.getAntherUser("teamUser"));
        team = teamRepository.save(TeamFixture.create(teamUser));
        recruitment = recruitmentRepository.save(RecruitmentFixture.create(team));
    }

    @Test
    void 채용과정_생성_성공() {
        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user);

        recruitmentProcess = recruitmentProcessRepository.save(recruitmentProcess);

        RecruitmentProcess findRecruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcess.getId()).orElseThrow();

        assertThat(findRecruitmentProcess.getId()).isNotNull();
        assertThat(findRecruitmentProcess.getUser()).isEqualTo(user);
        assertThat(findRecruitmentProcess.getRecruitment()).isEqualTo(recruitment);
        assertThat(findRecruitmentProcess.getStatus()).isEqualTo(WAITING);
        assertThat(findRecruitmentProcess.getCreatedAt()).isNotNull();
    }
}
