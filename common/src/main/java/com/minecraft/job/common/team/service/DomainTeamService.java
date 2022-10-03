package com.minecraft.job.common.team.service;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.minecraft.job.common.support.Preconditions.require;

@Service
@Transactional
@RequiredArgsConstructor
public class DomainTeamService implements TeamService {

    private final TeamRepository teamRepository;

    private final UserRepository userRepository;

    @Override
    public Team create(Long userId, String name, String description, String logo, Long memberNum) {
        User user = userRepository.findById(userId).orElseThrow();

        Team team = Team.create(name, description, logo, memberNum, user);

        return teamRepository.save(team);
    }

    @Override
    public void update(Long teamId, Long userId, String name, String description, String logo, Long memberNum) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        require(team.ofUser(user));

        team.update(name, description, logo, memberNum);
    }

    @Override
    public void inactivate(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        require(team.ofUser(user));

        team.inactivate();
    }

    @Override
    public void activate(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        require(team.ofUser(user));

        team.activate();
    }
}
