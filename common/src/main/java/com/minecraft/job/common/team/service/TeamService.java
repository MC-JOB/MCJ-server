package com.minecraft.job.common.team.service;

import com.minecraft.job.common.team.domain.Team;

public interface TeamService {

    Team create(Long userId, String name, String description, String logo, Long memberNum);

    void update(Long teamId, Long userId, String name, String description, String logo, Long memberNum);
}
