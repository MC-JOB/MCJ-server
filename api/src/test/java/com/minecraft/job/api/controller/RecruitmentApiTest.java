package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.RecruitmentClosedAtExtendDto;
import com.minecraft.job.api.controller.dto.RecruitmentDeleteDto;
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

import java.time.LocalDateTime;

import static com.minecraft.job.api.controller.dto.RecruitmentActivateDto.RecruitmentActivateRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentClosedAtExtendDto.*;
import static com.minecraft.job.api.controller.dto.RecruitmentCreateDto.RecruitmentCreateRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentDeleteDto.*;
import static com.minecraft.job.api.controller.dto.RecruitmentInactivateDto.RecruitmentInactivateRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentUpdateDto.RecruitmentUpdateRequest;
import static com.minecraft.job.common.recruitment.domain.RecruitmentStatus.*;
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

    @Test
    void 채용공고_활성화_성공() throws Exception {
        RecruitmentActivateRequest recruitmentActivateRequest = new RecruitmentActivateRequest(recruitment.getId(), user.getId(), team.getId(), LocalDateTime.now().plusMinutes(1));

        mockMvc.perform(post("/recruitment/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recruitmentActivateRequest)))
                .andExpectAll(
                        status().isOk()
                );

        Recruitment findRecruitment = recruitmentRepository.findById(recruitment.getId()).orElseThrow();

        assertThat(findRecruitment.getStatus()).isEqualTo(ACTIVATED);
    }

    @Test
    void 채용공고_삭제_성공() throws Exception {
        RecruitmentDeleteRequest recruitmentDeleteRequest = new RecruitmentDeleteRequest(recruitment.getId(), user.getId(), team.getId());

        mockMvc.perform(post("/recruitment/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recruitmentDeleteRequest)))
                .andExpectAll(
                        status().isOk()
                );

        Recruitment findRecruitment = recruitmentRepository.findById(recruitment.getId()).orElseThrow();

        assertThat(findRecruitment.getStatus()).isEqualTo(DELETED);
    }

    @Test
    void 채용공고_기간연장_성공() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(1);

        recruitment.activate(localDateTime);

        RecruitmentClosedAtExtendRequest recruitmentClosedAtExtendRequest = new RecruitmentClosedAtExtendRequest(recruitment.getId(), user.getId(), team.getId(), localDateTime.plusMinutes(1));

        mockMvc.perform(post("/recruitment/closedAtExtend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recruitmentClosedAtExtendRequest)))
                .andExpectAll(
                        status().isOk()
                );

        Recruitment findRecruitment = recruitmentRepository.findById(recruitment.getId()).orElseThrow();

        assertThat(findRecruitment.getClosedAt()).isEqualTo(localDateTime.plusMinutes(1));
    }
}
