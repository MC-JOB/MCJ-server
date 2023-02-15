package com.minecraft.job.common.recruitment.domain;

import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.UserFixture;
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

import static com.minecraft.job.common.recruitment.domain.RecruitmentStatus.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class RecruitmentRepositoryTest {

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    private Team team;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());
        team = teamRepository.save(TeamFixture.create(user));
    }

    @Test
    void 채용공고_생성_성공() {
        Recruitment recruitment = Recruitment.create("title", "content", team);

        recruitment = recruitmentRepository.save(recruitment);

        Recruitment findRecruitment = recruitmentRepository.findById(recruitment.getId()).orElseThrow();

        assertThat(findRecruitment.getId()).isNotNull();
        assertThat(findRecruitment.getTitle()).isEqualTo("title");
        assertThat(findRecruitment.getContent()).isEqualTo("content");
        assertThat(findRecruitment.getTeam()).isEqualTo(team);
        assertThat(findRecruitment.getStatus()).isEqualTo(CREATED);
        assertThat(findRecruitment.getCreatedAt()).isNotNull();
    }

    @Test
    void 채용공고_리스트_조회_성공__제목이_포함되는_경우() {
        String title = "title";
        채용공고_목록_생성(title, "content", team);

        Specification<Recruitment> spec = Specification.where(RecruitmentSpecification.likeTitle(title));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Recruitment> findRecruitmentList = recruitmentRepository.findAll(spec, pageRequest);

        assertThat(findRecruitmentList.getNumberOfElements()).isEqualTo(10);
        for (Recruitment recruitment : findRecruitmentList) {
            assertThat(recruitment.getTitle()).contains(title);
        }
    }

    @Test
    void 채용공고_리스트_조회_성공__내용이_포함되는_경우() {
        String content = "content";
        채용공고_목록_생성("title", content, team);

        Specification<Recruitment> spec = Specification.where(RecruitmentSpecification.likeContent(content));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Recruitment> findRecruitmentList = recruitmentRepository.findAll(spec, pageRequest);

        assertThat(findRecruitmentList.getNumberOfElements()).isEqualTo(10);
        for (Recruitment recruitment : findRecruitmentList) {
            assertThat(recruitment.getContent()).contains(content);
        }
    }

    @Test
    void 채용공고_리스트_조회_성공__팀이_일치하는_경우() {
        채용공고_목록_생성("title", "content", team);

        Specification<Recruitment> spec = Specification.where(RecruitmentSpecification.equalTeam(team));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Recruitment> findRecruitmentList = recruitmentRepository.findAll(spec, pageRequest);

        assertThat(findRecruitmentList.getNumberOfElements()).isEqualTo(10);
        for (Recruitment recruitment : findRecruitmentList) {
            assertThat(recruitment.getTeam()).isEqualTo(team);
        }
    }

    @Test
    void 채용공고_리스트_조회_성공__페이징_처리() {
        채용공고_목록_생성("title", "content", team);

        Specification<Recruitment> spec = Specification.where(RecruitmentSpecification.likeContent("content"));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Recruitment> findRecruitmentList = recruitmentRepository.findAll(spec, pageRequest);

        assertThat(findRecruitmentList.getTotalPages()).isEqualTo(2);
    }

    @Test
    void 채용공고_조회_성공__팀_아이디가_일치하는_경우() {
        Recruitment recruitment = Recruitment.create("title", "content", team);

        recruitmentRepository.save(recruitment);

        Optional<Recruitment> findRecruitment = recruitmentRepository.findByTeam_Id(team.getId());

        assertThat(findRecruitment).isNotNull();
        assertThat(findRecruitment.get().getTeam().getId()).isEqualTo(team.getId());
    }

    @Test
    void 채용공고_조회_실패__팀_아이디가_일치하지_않는_경우() {
        Recruitment recruitment = Recruitment.create("title", "content", team);

        recruitmentRepository.save(recruitment);

        Optional<Recruitment> findRecruitment = recruitmentRepository.findByTeam_Id(2L);

        assertThat(findRecruitment).isEmpty();
    }

    private void 채용공고_목록_생성(String title, String content, Team team) {
        for (int i = 1; i <= 20; i++) {
            Recruitment recruitment = Recruitment.create(title + i, content + i, team);
            recruitmentRepository.save(recruitment);
        }
    }
}
