package com.minecraft.job.common.fixture;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.teamlogo.domain.TeamLogo;

public class TeamLogoFixture {

    public static TeamLogo create(Team team) {
        return TeamLogo.create("name", "savedName", 1L, team);
    }
}
