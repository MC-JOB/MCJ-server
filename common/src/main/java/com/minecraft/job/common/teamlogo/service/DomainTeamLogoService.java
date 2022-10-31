package com.minecraft.job.common.teamlogo.service;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.teamlogo.domain.TeamLogo;
import com.minecraft.job.common.teamlogo.domain.TeamLogoRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.minecraft.job.common.support.Preconditions.require;


@Service
@Transactional
@RequiredArgsConstructor
public class DomainTeamLogoService implements TeamLogoService {

    private final TeamLogoRepository teamLogoRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
    public TeamLogo create(Long teamId, Long userId, String name, String savedName, Long size) {
        User user = userRepository.getReferenceById(userId);
        Team team = teamRepository.getReferenceById(teamId);

        require(team.ofUser(user));

        TeamLogo teamLogo = TeamLogo.create(name, savedName, size, team);

        return teamLogoRepository.save(teamLogo);
    }
}
