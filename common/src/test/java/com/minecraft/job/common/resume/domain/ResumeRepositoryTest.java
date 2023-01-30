package com.minecraft.job.common.resume.domain;

import com.minecraft.job.common.fixture.UserFixture;
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

import static com.minecraft.job.common.resume.domain.ResumeStatue.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class ResumeRepositoryTest {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());
    }

    @Test
    void 이력서_생성_성공() {
        Resume resume = Resume.create("title", "content", "trainingHistory", user);

        resume = resumeRepository.save(resume);

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getId()).isNotNull();
        assertThat(findResume.getTitle()).isEqualTo("title");
        assertThat(findResume.getContent()).isEqualTo("content");
        assertThat(findResume.getTrainingHistory()).isEqualTo("trainingHistory");
        assertThat(findResume.getUser()).isEqualTo(user);
        assertThat(findResume.getStatus()).isEqualTo(CREATED);
        assertThat(findResume.getCreatedAt()).isNotNull();
    }

    @Test
    void 이력서_리스트_조회_성공__제목이_포함되는_경우() {
        String title = "title";
        이력서_목록_생성(20, title, "content", "trainingHistory", user);

        Specification<Resume> spec = Specification.where(ResumeSpecification.likeTitle(title));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Resume> findResumeList = resumeRepository.findAll(spec, pageRequest);

        assertThat(findResumeList.getNumberOfElements()).isEqualTo(10);
        for (Resume resume : findResumeList) {
            assertThat(resume.getTitle()).contains(title);
        }
    }

    @Test
    void 이력서_리스트_조회_성공__내용이_포함되는_경우() {
        String content = "content";
        이력서_목록_생성(20, "title", content, "trainingHistory", user);

        Specification<Resume> spec = Specification.where(ResumeSpecification.likeContent(content));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Resume> findResumeList = resumeRepository.findAll(spec, pageRequest);

        assertThat(findResumeList.getNumberOfElements()).isEqualTo(10);
        for (Resume resume : findResumeList) {
            assertThat(resume.getContent()).contains(content);
        }
    }

    @Test
    void 이력서_리스트_조회_성공__교육이력이_포함되는_경우() {
        String trainingHistory = "trainingHistory";
        이력서_목록_생성(20, "title", "content", trainingHistory, user);

        Specification<Resume> spec = Specification.where(ResumeSpecification.likeTrainingHistory(trainingHistory));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Resume> findResumeList = resumeRepository.findAll(spec, pageRequest);

        assertThat(findResumeList.getNumberOfElements()).isEqualTo(10);
        for (Resume resume : findResumeList) {
            assertThat(resume.getTrainingHistory()).contains(trainingHistory);
        }
    }

    @Test
    void 이력서_리스트_조회_성공__유저가_일치하는_경우() {
        이력서_목록_생성(20, "title", "content", "trainingHistory", user);

        Specification<Resume> spec = Specification.where(ResumeSpecification.equalUser(user));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Resume> findResumeList = resumeRepository.findAll(spec, pageRequest);

        assertThat(findResumeList.getNumberOfElements()).isEqualTo(10);
        for (Resume resume : findResumeList) {
            assertThat(resume.getUser()).isEqualTo(user);
        }
    }

    @Test
    void 이력서_리스트_조회_성공__페이징_처리() {
        이력서_목록_생성(20, "title", "content", "trainingHistory", user);

        Specification<Resume> spec = Specification.where(ResumeSpecification.likeTitle("title"));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Resume> findResumeList = resumeRepository.findAll(spec, pageRequest);

        assertThat(findResumeList.getTotalPages()).isEqualTo(2);
    }

    private void 이력서_목록_생성(int count, String title, String content, String trainingHistory, User user) {
        for (int i = 1; i <= count; i++) {
            Resume resume = Resume.create(title + i, content + i, trainingHistory, user);
            resumeRepository.save(resume);
        }
    }
}
