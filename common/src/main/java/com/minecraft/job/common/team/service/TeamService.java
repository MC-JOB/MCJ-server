package com.minecraft.job.common.team.service;

import com.minecraft.job.common.team.domain.Team;

public interface TeamService {

    Team create(
            String name, String email, String password,
            String description, String logo, Long memberNum
    );
}
