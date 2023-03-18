package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.TeamActivateDto.TeamActivateRequest;
import com.minecraft.job.api.controller.dto.TeamCreateDto.TeamCreateRequest;
import com.minecraft.job.api.controller.dto.TeamInactivateDto.TeamInactivateRequest;
import com.minecraft.job.api.controller.dto.TeamUpdateDto.TeamUpdateRequest;
import com.minecraft.job.api.fixture.EmailAuthFixture;
import com.minecraft.job.api.fixture.TeamFixture;
import com.minecraft.job.api.support.ApiTest;
import com.minecraft.job.common.emailauth.domain.EmailAuthRepository;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.team.domain.TeamSearchType;
import com.minecraft.job.common.team.service.TeamService;
import com.minecraft.job.common.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.minecraft.job.api.controller.dto.TeamGetDetailDto.TeamGetDetailRequest;
import static com.minecraft.job.api.controller.dto.TeamGetListDto.TeamGetListRequest;
import static com.minecraft.job.common.team.domain.TeamStatus.ACTIVATED;
import static com.minecraft.job.common.team.domain.TeamStatus.INACTIVATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TeamApiTest extends ApiTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @Autowired
    private TeamService teamService;

    private Team team;
    private User user;

    @BeforeEach
    void setUp() {
        user = prepareLoggedInUser("setUp");
        team = teamRepository.save(TeamFixture.create(user));
        emailAuthRepository.save(EmailAuthFixture.getValidatedEmailAuth(user.getEmail()));
    }

    @Test
    @WithUserDetails
    void 팀_생성_성공() throws Exception {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest("teamName", "description", 10L);

        mockMvc.perform(post("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamCreateRequest)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.team.id").isNotEmpty(),
                        jsonPath("$.team.name").value("teamName")
                ).andDo(document("team/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @WithUserDetails
    void 팀_수정_성공() throws Exception {
        TeamUpdateRequest teamUpdateRequest = new TeamUpdateRequest(team.getId(), user.getId(), "updateName", "updateDescription", 1L);

        mockMvc.perform(post("/team/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamUpdateRequest)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("team/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getName()).isEqualTo("updateName");
        assertThat(findTeam.getDescription()).isEqualTo("updateDescription");
        assertThat(findTeam.getMemberNum()).isEqualTo(1L);
    }

    @Test
    @WithUserDetails
    void 팀_비활성화_성공() throws Exception {
        TeamInactivateRequest teamInactivateRequest = new TeamInactivateRequest(team.getId(), user.getId());

        mockMvc.perform(post("/team/inactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamInactivateRequest)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("team/inactivate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getStatus()).isEqualTo(INACTIVATED);
    }

    @Test
    @WithUserDetails
    void 팀_활성화_성공() throws Exception {
        TeamActivateRequest teamActivateRequest = new TeamActivateRequest(team.getId(), user.getId());

        teamService.inactivate(team.getId(), user.getId());

        mockMvc.perform(post("/team/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamActivateRequest)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("team/activate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getStatus()).isEqualTo(ACTIVATED);
    }

    @Test
    @WithUserDetails
    void 팀_상세_조회_성공() throws Exception {
        TeamGetDetailRequest req = new TeamGetDetailRequest(team.getId());

        mockMvc.perform(get("/team/getMyTeam")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(status().isOk())
                .andDo(document("team/getMyTeam",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam).isNotNull();
    }

    @Test
    @WithUserDetails
    void 팀_목록_조회_성공() throws Exception {
        TeamGetListRequest req = new TeamGetListRequest(TeamSearchType.ALL, "", 0, 10, user.getId());

        mockMvc.perform(get("/team/getMyTeamList")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(status().isOk())
                .andDo(document("team/getMyTeamList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Specification<Team> spec = Specification.where(null);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Team> findTeamList = teamRepository.findAll(spec, pageable);

        assertThat(findTeamList).isNotNull();
    }
}
