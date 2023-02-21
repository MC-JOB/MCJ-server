package com.minecraft.job.common.resume.service;

import com.minecraft.job.common.fixture.ResumeFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static com.minecraft.job.common.resume.domain.ResumeSearchType.*;
import static com.minecraft.job.common.resume.domain.ResumeStatue.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
@Transactional
class DomainResumeServiceTest {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    private Resume resume;
    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());
    }

    @Test
    void 이력서_생성_성공() {
        Resume resume = resumeService.create(user.getId(), "title", "content", "trainingHistory");

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getId()).isNotNull();
    }

    @Test
    void 이력서_수정_성공() {
        이력서_생성();

        resumeService.update(resume.getId(), user.getId(), "updateTitle", "updateContent", "updateTrainingHistory");

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getTitle()).isEqualTo("updateTitle");
        assertThat(findResume.getContent()).isEqualTo("updateContent");
        assertThat(findResume.getTrainingHistory()).isEqualTo("updateTrainingHistory");
    }

    @Test
    void 이력서_수정_실패__유저의_이력서가_아님() {
        이력서_생성();

        User fakeUser = userRepository.save(UserFixture.getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(() -> resumeService.update(resume.getId(), fakeUser.getId(), "updateTitle", "updateContent", "updateTrainingHistory"));
    }

    @Test
    void 이력서_활성화_성공() {
        이력서_생성();

        resumeService.activate(resume.getId(), user.getId());

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getStatus()).isEqualTo(ACTIVATED);
    }

    @Test
    void 이력서_활성화_실패__유저의_이력서가_아님() {
        이력서_생성();

        User fakeUser = userRepository.save(UserFixture.getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(() -> resumeService.activate(resume.getId(), fakeUser.getId()));
    }

    @Test
    void 이력서_비활성화_성공() {
        이력서_생성();

        resumeService.inactivate(resume.getId(), user.getId());

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getStatus()).isEqualTo(INACTIVATED);
    }

    @Test
    void 이력서_비활성화_실패__유저의_이력서가_아님() {
        이력서_생성();

        User fakeUser = userRepository.save(UserFixture.getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(() -> resumeService.inactivate(resume.getId(), fakeUser.getId()));
    }

    @Test
    void 이력서_삭제_성공() {
        이력서_생성();

        resumeService.delete(resume.getId(), user.getId());

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getStatus()).isEqualTo(DELETED);
    }

    @Test
    void 이력서_삭제_실패__유저의_이력서가_아님() {
        이력서_생성();

        User fakeUser = userRepository.save(UserFixture.getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(() -> resumeService.delete(resume.getId(), fakeUser.getId()));
    }

    @Test
    void 이력서_리스트_조회_성공__제목이_포함되는_경우() {
        String title = "title";
        이력서_목록_생성(20, title, "content", "trainingHistory", user);

        PageRequest pageable = PageRequest.of(0, 10);

        Page<Resume> findResumeList = resumeService.getResumes(TITLE, title, pageable);

        for (Resume resume : findResumeList) {
            assertThat(resume.getTitle()).contains(title);
        }
    }

    @Test
    void 이력서_리스트_조회_성공__내용이_포함되는_경우() {
        String content = "content";
        이력서_목록_생성(20, "title", content, "trainingHistory", user);

        PageRequest pageable = PageRequest.of(0, 10);

        Page<Resume> findResumeList = resumeService.getResumes(CONTENT, content, pageable);

        for (Resume resume : findResumeList) {
            assertThat(resume.getContent()).contains(content);
        }
    }

    @Test
    void 이력서_리스트_조회_성공__교육이력이_포함되는_경우() {
        String trainingHistory = "trainingHistory";
        이력서_목록_생성(20, "title", "content", trainingHistory, user);

        PageRequest pageable = PageRequest.of(0, 10);

        Page<Resume> findResumeList = resumeService.getResumes(TRAININGHISTORY, trainingHistory, pageable);

        for (Resume resume : findResumeList) {
            assertThat(resume.getTrainingHistory()).contains(trainingHistory);
        }
    }

    @Test
    void 이력서_리스트_조회_성공__유저가_일치하는_경우() {
        이력서_목록_생성(20, "title", "content", "trainingHistory", user);

        PageRequest pageable = PageRequest.of(0, 10);

        Page<Resume> findResumeList = resumeService.getResumes(USER, user.getNickname(), pageable);

        for (Resume resume : findResumeList) {
            assertThat(resume.getUser()).isEqualTo(user);
        }
    }

    @Test
    void 이력서_리스트_조회_성공__페이징_처리() {
        이력서_목록_생성(20, "title", "content", "trainingHistory", user);

        PageRequest pageable = PageRequest.of(0, 10);

        Page<Resume> findResumeList = resumeService.getResumes(TITLE, "title", pageable);

        assertThat(findResumeList.getTotalPages()).isEqualTo(2);
    }

    @Test
    void 이력서_조회_성공() {
        이력서_생성();

        Resume findResume = resumeService.getResume(user.getId());

        assertThat(findResume.getUser().getId()).isEqualTo(user.getId());
    }

    private void 이력서_생성() {
        resume = resumeRepository.save(ResumeFixture.create(user));
    }

    private void 이력서_목록_생성(int count, String title, String content, String trainingHistory, User user) {
        for (int i = 1; i <= count; i++) {
            Resume resume = Resume.create(title + i, content + i, trainingHistory, user);
            resumeRepository.save(resume);
        }
    }
}
