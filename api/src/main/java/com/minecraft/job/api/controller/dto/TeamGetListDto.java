package com.minecraft.job.api.controller.dto;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamSearchType;
import org.springframework.data.domain.Page;

public class TeamGetListDto {

    public record TeamGetListRequest(
            TeamSearchType searchType,
            String searchName,
            int page,
            int size
    ) {
    }

    public record TeamGetListResponse(TeamGetListData teamList) {

        public static TeamGetListResponse getTeamList(TeamGetListData teamGetListData) {
            return new TeamGetListResponse(teamGetListData);
        }
    }

    public record TeamGetListData(Page<Team> teamList) {

        public static TeamGetListData getTeamList(Page<Team> teamList) {

            return new TeamGetListData(teamList);
        }
    }
}
