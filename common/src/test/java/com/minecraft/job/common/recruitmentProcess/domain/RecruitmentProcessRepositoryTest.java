package com.minecraft.job.common.recruitmentProcess.domain;

import com.minecraft.job.common.fixture.RecruitmentFixture;
import com.minecraft.job.common.fixture.ResumeFixture;
import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.domain.RecruitmentRepository;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeRepository;
import com.minecraft.job.common.resume.domain.ResumeSpecification;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

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

    @Autowired
    private ResumeRepository resumeRepository;

    private Recruitment recruitment;
    private User user;
    private User teamUser;
    private Team team;
    private Resume resume;

    private RecruitmentProcess recruitmentProcess;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());
        teamUser = userRepository.save(UserFixture.getAntherUser("teamUser"));
        team = teamRepository.save(TeamFixture.create(teamUser));
        recruitment = recruitmentRepository.save(RecruitmentFixture.create(team));
        resume = resumeRepository.save(ResumeFixture.create(user));
    }

    @Test
    void 채용과정_생성_성공() {
        채용과정_생성();

        RecruitmentProcess findRecruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcess.getId()).orElseThrow();

        assertThat(findRecruitmentProcess.getId()).isNotNull();
        assertThat(findRecruitmentProcess.getUser()).isEqualTo(user);
        assertThat(findRecruitmentProcess.getRecruitment()).isEqualTo(recruitment);
        assertThat(findRecruitmentProcess.getStatus()).isEqualTo(WAITING);
        assertThat(findRecruitmentProcess.getCreatedAt()).isNotNull();
    }

    @Test
    void 채용과정_조회_성공__유저_아이디가_일치하는_경우() {
        채용과정_생성();

        Optional<RecruitmentProcess> findRecruitmentProcess = recruitmentProcessRepository.findByUser_Id(user.getId());

        assertThat(findRecruitmentProcess).isNotNull();
        assertThat(findRecruitmentProcess.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void 채용과정_조회_실패__유저_아이디가_일치하지_않는_경우() {
        채용과정_생성();

        Optional<RecruitmentProcess> findRecruitmentProcess = recruitmentProcessRepository.findByUser_Id(2L);

        assertThat(findRecruitmentProcess).isEmpty();
    }

    @Test
    void 채용과정_리스트_조회_성공__유저가_일치하는_경우() {
        채용과정_생성();

        Specification<RecruitmentProcess> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user));

        PageRequest pageable = PageRequest.of(0, 10);

        Page<RecruitmentProcess> findRecruitmentProcessList = recruitmentProcessRepository.findAll(spec, pageable);

        assertThat(findRecruitmentProcessList.getNumberOfElements()).isEqualTo(1);
        for (RecruitmentProcess recruitmentProcess : findRecruitmentProcessList) {
            assertThat(recruitmentProcess.getUser()).isEqualTo(user);
        }
    }

    private void 채용과정_생성() {
        recruitmentProcess = recruitmentProcessRepository.save(RecruitmentProcess.create(recruitment, user, resume));
    }
}
