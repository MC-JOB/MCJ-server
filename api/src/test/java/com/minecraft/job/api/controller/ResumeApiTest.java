package com.minecraft.job.api.controller;

import com.minecraft.job.api.fixture.UserFixture;
import com.minecraft.job.api.support.ApiTest;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static com.minecraft.job.api.controller.dto.ResumeCreateDto.ResumeCreateRequest;
import static com.minecraft.job.common.resume.domain.ResumeStatue.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResumeApiTest extends ApiTest {

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
    void 이력서_생성_성공() throws Exception {
        ResumeCreateRequest req = new ResumeCreateRequest(user.getId(), "title", "content", "trainingHistory");

        mockMvc.perform(post("/resume")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.resume.id").isNotEmpty(),
                        jsonPath("$.resume.title").value("title")
                );

        Resume findResume = resumeRepository.findAll().get(0);

        assertThat(findResume.getId()).isNotNull();
        assertThat(findResume.getTitle()).isEqualTo("title");
        assertThat(findResume.getContent()).isEqualTo("content");
        assertThat(findResume.getTrainingHistory()).isEqualTo("trainingHistory");
        assertThat(findResume.getUser()).isEqualTo(user);
        assertThat(findResume.getStatus()).isEqualTo(CREATED);
        assertThat(findResume.getCreatedAt()).isNotNull();
    }
}
