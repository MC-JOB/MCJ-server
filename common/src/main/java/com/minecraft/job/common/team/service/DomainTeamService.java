package com.minecraft.job.common.team.service;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DomainTeamService implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public Team create(
            String name, String email, String password,
            String description, String logo, Long memberNum
    ) {
        Team team = Team.create(name, email, password, description, logo, memberNum);

        return teamRepository.save(team);
    }
}
