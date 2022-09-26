package com.minecraft.job.common.fixture;

import com.minecraft.job.common.team.domain.Team;

public class TeamFixture {

    public static Team create(){
        return Team.create("name", "email", "password", "description", "logo", 5L);
    }
}
