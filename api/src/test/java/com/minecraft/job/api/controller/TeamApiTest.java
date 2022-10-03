package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.TeamCreateDto.TeamCreateRequest;
import com.minecraft.job.api.fixture.UserFixture;
import com.minecraft.job.api.support.ApiTest;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TeamApiTest extends ApiTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());
    }

    @Test
    void 팀_생성_성공() throws Exception {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(user.getId(), "name", "desciption", "logo", 10L);

        mockMvc.perform(post("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamCreateRequest)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.team.id").isNotEmpty(),
                        jsonPath("$.team.name").value("name")
                );
    }
}
