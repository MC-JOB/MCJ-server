package com.minecraft.job.common.fixture;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.user.domain.User;

public class TeamFixture {

    public static Team create(User user) {
        return Team.create("name", "description", "logo", 5L, user);
    }

    public static Team getFakeTeam(User user)  {
        return Team.create("fakeName", "fakeDescription", "fakeLogo", 5L, user);
    }
}
