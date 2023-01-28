package com.minecraft.job.common.team.service;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.team.domain.TeamSearchType;
import com.minecraft.job.common.team.domain.TeamSpecification;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
    public Team create(Long userId, String name, String description, Long memberNum) {
        User user = userRepository.findById(userId).orElseThrow();

        Team team = Team.create(name, description, memberNum, user);

        return teamRepository.save(team);
    }

    @Override
    public void applyAveragePoint(Long teamId, Double averagePoint) {
        Team team = teamRepository.findById(teamId).orElseThrow();

        team.applyAveragePoint(averagePoint);
    }

    @Override
    public void update(Long teamId, Long userId, String name, String description, Long memberNum) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        require(team.ofUser(user));

        team.update(name, description, memberNum);
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

    @Override
    public Page<Team> getTeams(TeamSearchType searchType, String searchName, int page) {

        Specification<Team> spec = getTeamSpecification(searchType, searchName);

        PageRequest pageRequest = PageRequest.of(page, 10);

        return teamRepository.findAll(spec, pageRequest);
    }

    private Specification<Team> getTeamSpecification(TeamSearchType searchType, String searchName) {
        Specification<Team> spec = null;

        if (searchType == TeamSearchType.NAME) {
            spec = Specification.where(TeamSpecification.likeName(searchName));
        }
        if (searchType == TeamSearchType.DESCRIPTION) {
            spec = Specification.where(TeamSpecification.likeDescription(searchName));
        }
        if (searchType == TeamSearchType.USER) {
            User user = userRepository.findByNickname(searchName);
            spec = Specification.where(TeamSpecification.equalUser(user));
        }
        return spec;
    }
}
