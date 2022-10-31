package com.minecraft.job.api.fixture;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.user.domain.User;

public class TeamFixture {

    public static Team create(User user) {
        return Team.create("name", "description", 5L, user);
    }

    public static Team getFakeTeam(User user) {
        return Team.create("fakeName", "fakeDescription", 5L, user);
    }
}
