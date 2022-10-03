package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.TeamCreateDto.TeamCreateData;
import com.minecraft.job.api.controller.dto.TeamCreateDto.TeamCreateRequest;
import com.minecraft.job.api.controller.dto.TeamCreateDto.TeamCreateResponse;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.minecraft.job.api.controller.dto.TeamUpdateDto.TeamUpdateRequest;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamApi {

    private final TeamService teamService;

    @PostMapping
    public TeamCreateResponse create(@RequestBody TeamCreateRequest req) {
        Team team = teamService.create(req.userId(), req.name(), req.description(), req.logo(), req.memberNum());

        return TeamCreateResponse.create(TeamCreateData.create(team));
    }

    @PostMapping("/update")
    public void update(@RequestBody TeamUpdateRequest req) {

        teamService.update(req.teamId(), req.userId(), req.name(), req.description(), req.logo(), req.memberNum());
    }
}
