package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.TeamActivateDto.TeamActivateRequest;
import com.minecraft.job.api.controller.dto.TeamCreateDto.TeamCreateData;
import com.minecraft.job.api.controller.dto.TeamCreateDto.TeamCreateRequest;
import com.minecraft.job.api.controller.dto.TeamCreateDto.TeamCreateResponse;
import com.minecraft.job.api.controller.dto.TeamInactivateDto.TeamInactivateRequest;
import com.minecraft.job.api.security.user.DefaultMcjUser;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.minecraft.job.api.controller.dto.TeamGetDetailDto.*;
import static com.minecraft.job.api.controller.dto.TeamGetListDto.*;
import static com.minecraft.job.api.controller.dto.TeamUpdateDto.TeamUpdateRequest;

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
    public void update(@RequestBody TeamUpdateRequest req) {

        teamService.update(req.teamId(), req.userId(), req.name(), req.description(), req.memberNum());
    }

    @PostMapping("/inactivate")
    public void inactivate(@RequestBody TeamInactivateRequest req) {

        teamService.inactivate(req.teamId(), req.userId());
    }

    @PostMapping("/activate")
    public void activate(@RequestBody TeamActivateRequest req) {

        teamService.activate(req.teamId(), req.userId());
    }

    @GetMapping("/getMyTeam")
    public TeamGetDetailResponse getMyTeamDetail(@RequestBody TeamGetDetailRequest req) {
        Team team = teamService.getTeam(req.teamId());

        return TeamGetDetailResponse.getTeam(TeamGetDetailData.getTeam(team));
    }

    @GetMapping("/getMyTeamList")
    public TeamGetListResponse getMyTeamList(@RequestBody TeamGetListRequest req) {
        PageRequest pageable = PageRequest.of(req.page(), req.size());
        Page<Team> teamList = teamService.getMyTeamList(req.searchType(), req.searchName(), pageable, req.userId());

        return TeamGetListResponse.getTeamList(TeamGetListData.getTeamList(teamList));
    }
}
