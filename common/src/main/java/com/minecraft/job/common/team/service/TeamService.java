package com.minecraft.job.common.team.service;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamSearchType;
import org.springframework.data.domain.Page;

public interface TeamService {

    Team create(Long userId, String name, String description, Long memberNum);

    void applyAveragePoint(Long teamId, Double averagePoint);

    void update(Long teamId, Long userId, String name, String description, Long memberNum);

    void inactivate(Long teamId, Long userId);

    void activate(Long teamId, Long userId);

    Page<Team> getTeams(TeamSearchType searchType, String searchName, int page);
}
