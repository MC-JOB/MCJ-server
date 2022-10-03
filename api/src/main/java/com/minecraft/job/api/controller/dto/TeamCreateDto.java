package com.minecraft.job.api.controller.dto;

import com.minecraft.job.common.team.domain.Team;

public class TeamCreateDto {

    public record TeamCreateRequest(Long userId, String name, String description, String logo, Long memberNum) {
    }

    public record TeamCreateResponse(TeamCreateData team) {

        public static TeamCreateResponse create(TeamCreateData team) {
            return new TeamCreateResponse(team);
        }
    }

    public record TeamCreateData(Long id, String name) {

        public static TeamCreateData create(Team team) {
            return new TeamCreateData(team.getId(), team.getName());
        }
    }
}
