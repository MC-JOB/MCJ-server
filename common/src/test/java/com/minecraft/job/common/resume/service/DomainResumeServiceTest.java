package com.minecraft.job.common.resume.service;

import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DomainResumeServiceTest {

    @Autowired
    private ResumeService resumeService;

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
        Resume resume = resumeService.create(user.getId(), "title", "content", "trainingHistory");

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getId()).isNotNull();
    }
}
