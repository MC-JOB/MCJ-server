package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.team.TeamActivateDto.TeamActivateRequest;
import com.minecraft.job.api.controller.dto.team.TeamCreateDto.TeamCreateData;
import com.minecraft.job.api.controller.dto.team.TeamCreateDto.TeamCreateRequest;
import com.minecraft.job.api.controller.dto.team.TeamCreateDto.TeamCreateResponse;
import com.minecraft.job.api.controller.dto.team.TeamInactivateDto.TeamInactivateRequest;
import com.minecraft.job.api.security.user.DefaultMcjUser;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.minecraft.job.api.controller.dto.team.TeamGetDetailDto.*;
import static com.minecraft.job.api.controller.dto.team.TeamGetListDto.*;
import static com.minecraft.job.api.controller.dto.team.TeamUpdateDto.TeamUpdateRequest;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamApi {

    private final TeamService teamService;

    @PostMapping
    public TeamCreateResponse create(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody TeamCreateRequest req
    ) {
        Team team = teamService.create(user.getId(), req.name(), req.description(), req.memberNum());

        return TeamCreateResponse.create(TeamCreateData.create(team));
    }

    @PostMapping("/update")
    public void update(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody TeamUpdateRequest req
    ) {
        teamService.update(req.teamId(), user.getId(), req.name(), req.description(), req.memberNum());
    }

    @PostMapping("/inactivate")
    public void inactivate(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody TeamInactivateRequest req
    ) {
        teamService.inactivate(req.teamId(), user.getId());
    }

    @PostMapping("/activate")
    public void activate(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody TeamActivateRequest req
    ) {
        teamService.activate(req.teamId(), user.getId());
    }

    @GetMapping("/getMyTeam")
    public TeamGetDetailResponse getMyTeamDetail(@RequestBody TeamGetDetailRequest req) {
        Team team = teamService.getTeam(req.teamId());

        return TeamGetDetailResponse.getTeam(TeamGetDetailData.getTeam(team));
    }

    @GetMapping("/getMyTeamList")
    public TeamGetListResponse getMyTeamList(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody TeamGetListRequest req
    ) {
        PageRequest pageable = PageRequest.of(req.page(), req.size());

        Page<Team> teamList = teamService.getMyTeamList(req.searchType(), req.searchName(), pageable, user.getId());

        return TeamGetListResponse.getTeamList(TeamGetListData.getTeamList(teamList));
    }
}
