package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.RecruitmentCreateDto;
import com.minecraft.job.api.fixture.TeamFixture;
import com.minecraft.job.api.fixture.UserFixture;
import com.minecraft.job.api.support.ApiTest;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static com.minecraft.job.api.controller.dto.RecruitmentCreateDto.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecruitmentApiTest extends ApiTest {

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
}
