package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.RecruitmentInactivateDto;
import com.minecraft.job.api.fixture.RecruitmentFixture;
import com.minecraft.job.api.fixture.TeamFixture;
import com.minecraft.job.api.fixture.UserFixture;
import com.minecraft.job.api.support.ApiTest;
import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.domain.RecruitmentRepository;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static com.minecraft.job.api.controller.dto.RecruitmentCreateDto.RecruitmentCreateRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentInactivateDto.*;
import static com.minecraft.job.api.controller.dto.RecruitmentUpdateDto.RecruitmentUpdateRequest;
import static com.minecraft.job.common.recruitment.domain.RecruitmentStatus.INACTIVATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecruitmentApiTest extends ApiTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    private Team team;
    private User user;
    private Recruitment recruitment;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());
        team = teamRepository.save(TeamFixture.create(user));
        recruitment = recruitmentRepository.save(RecruitmentFixture.create(team));
    }

    @Test
    void 채용공고_생성_성공() throws Exception {
        RecruitmentCreateRequest recruitmentCreateRequest = new RecruitmentCreateRequest(user.getId(), team.getId(), "title", "content");

        mockMvc.perform(post("/recruitment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recruitmentCreateRequest)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.recruitment.id").isNotEmpty(),
                        jsonPath("$.recruitment.title").value("title")
                );
    }

    @Test
    void 채용공고_수정_성공() throws Exception {
        RecruitmentUpdateRequest recruitmentUpdateRequest = new RecruitmentUpdateRequest(recruitment.getId(), user.getId(), team.getId(), "updateTitle", "updateContent");

        mockMvc.perform(post("/recruitment/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recruitmentUpdateRequest)))
                .andExpectAll(
                        status().isOk()
                );

        Recruitment findRecruitment = recruitmentRepository.findById(recruitment.getId()).orElseThrow();

        assertThat(findRecruitment.getTitle()).isEqualTo("updateTitle");
        assertThat(findRecruitment.getContent()).isEqualTo("updateContent");
    }

    @Test
    void 채용공고_비활성화_성공() throws Exception {
        RecruitmentInactivateRequest recruitmentInactivateRequest = new RecruitmentInactivateRequest(recruitment.getId(), user.getId(), team.getId());

        mockMvc.perform(post("/recruitment/inactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recruitmentInactivateRequest)))
                .andExpectAll(
                        status().isOk()
                );
        Recruitment findRecruitment = recruitmentRepository.findById(recruitment.getId()).orElseThrow();

        assertThat(findRecruitment.getStatus()).isEqualTo(INACTIVATED);
    }
}
