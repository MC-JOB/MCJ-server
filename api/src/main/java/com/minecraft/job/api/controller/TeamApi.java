package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.TeamActivateDto.TeamActivateRequest;
import com.minecraft.job.api.controller.dto.TeamCreateDto.TeamCreateData;
import com.minecraft.job.api.controller.dto.TeamCreateDto.TeamCreateRequest;
import com.minecraft.job.api.controller.dto.TeamCreateDto.TeamCreateResponse;
import com.minecraft.job.api.controller.dto.TeamGetDetailDto;
import com.minecraft.job.api.controller.dto.TeamInactivateDto.TeamInactivateRequest;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.minecraft.job.api.controller.dto.TeamGetDetailDto.*;
import static com.minecraft.job.api.controller.dto.TeamUpdateDto.TeamUpdateRequest;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamApi {

    private final TeamService teamService;

    @PostMapping
    public TeamCreateResponse create(@RequestBody TeamCreateRequest req) {
        Team team = teamService.create(req.userId(), req.name(), req.description(), req.memberNum());

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
        Team team = teamService.getTeam(req.userId());

        return TeamGetDetailResponse.getTeam(TeamGetDetailData.getTeam(team));
    }
}
