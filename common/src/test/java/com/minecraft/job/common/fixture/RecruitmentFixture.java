package com.minecraft.job.common.fixture;

import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.team.domain.Team;

public class RecruitmentFixture {

    public static Recruitment create(Team team) {
        return Recruitment.create("title", "content", team);
    }
}
