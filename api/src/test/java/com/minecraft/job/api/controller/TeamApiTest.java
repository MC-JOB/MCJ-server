package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.TeamCreateDto.TeamCreateRequest;
import com.minecraft.job.api.controller.dto.TeamUpdateDto.TeamUpdateRequest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TeamApiTest extends ApiTest {

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
    void 팀_생성_성공() throws Exception {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(user.getId(), "teamName", "description", "logo", 10L);

        mockMvc.perform(post("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamCreateRequest)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.team.id").isNotEmpty(),
                        jsonPath("$.team.name").value("teamName")
                );
    }

    @Test
    void 팀_수정_성공() throws Exception {
        TeamUpdateRequest teamUpdateRequest = new TeamUpdateRequest(team.getId(), user.getId(), "updateName", "updateDescription", "updateLogo", 1L);

        mockMvc.perform(post("/team/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamUpdateRequest)))
                .andExpectAll(
                        status().isOk()
                );

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getName()).isEqualTo("updateName");
        assertThat(findTeam.getDescription()).isEqualTo("updateDescription");
        assertThat(findTeam.getLogo()).isEqualTo("updateLogo");
        assertThat(findTeam.getMemberNum()).isEqualTo(1L);
    }
}
