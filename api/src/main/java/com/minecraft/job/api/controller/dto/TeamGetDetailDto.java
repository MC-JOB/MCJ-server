package com.minecraft.job.api.controller.dto;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamStatus;

import java.time.LocalDateTime;

public class TeamGetDetailDto {

    public record TeamGetDetailRequest(
            Long userId
    ) {
    }

    public record TeamGetDetailResponse(
            TeamGetDetailData team
    ) {
        public static TeamGetDetailResponse getTeam(TeamGetDetailData teamGetDetailData) {
            return new TeamGetDetailResponse(teamGetDetailData);
        }
    }

    public record TeamGetDetailData(
            Long id, String name, String description, Long memberNum, TeamStatus status,
            LocalDateTime createdAt, Double averagePoint
    ) {
        public static TeamGetDetailData getTeam(Team team) {
            return new TeamGetDetailData(
                    team.getId(), team.getName(), team.getDescription(), team.getMemberNum(),
                    team.getStatus(), team.getCreatedAt(), team.getAveragePoint()
            );
        }
    }
}
