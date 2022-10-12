package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.ReviewCreateDto.ReviewCreateRequest;
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

class ReviewApiTest extends ApiTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    private User user;
    private Team team;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());

        User leader = userRepository.save(UserFixture.getAntherUser("leader"));
        team = teamRepository.save(TeamFixture.create(leader));
    }

    @Test
    void 리뷰_생성_성공() throws Exception {
        ReviewCreateRequest req = new ReviewCreateRequest(user.getId(), team.getId(), "content", 3L);

        mockMvc.perform(post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.review.id").isNotEmpty(),
                        jsonPath("$.review.score").value(3L),
                        jsonPath("$.averagePoint").value(3L)
                );

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getAveragePoint()).isEqualTo(3L);
    }
}
