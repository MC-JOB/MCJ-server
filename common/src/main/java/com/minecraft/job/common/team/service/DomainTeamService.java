package com.minecraft.job.common.team.service;

import com.minecraft.job.common.emailauth.domain.EmailAuth;
import com.minecraft.job.common.emailauth.domain.EmailAuthRepository;
import com.minecraft.job.common.resume.domain.ResumeSearchType;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.team.domain.TeamSearchType;
import com.minecraft.job.common.team.domain.TeamSpecification;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.minecraft.job.common.support.Preconditions.check;
import static com.minecraft.job.common.support.Preconditions.require;

@Service
@Transactional
@RequiredArgsConstructor
public class DomainTeamService implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final EmailAuthRepository emailAuthRepository;

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
        EmailAuth emailAuth = emailAuthRepository.findByEmail(user.getEmail()).orElseThrow();

        require(team.ofUser(user));

        check(emailAuth.isValidated());

        team.inactivate();
    }

    @Override
    public void activate(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        EmailAuth emailAuth = emailAuthRepository.findByEmail(user.getEmail()).orElseThrow();

        require(team.ofUser(user));

        check(emailAuth.isValidated());

        team.activate();
    }

    @Override
    public Page<Team> getTeams(TeamSearchType searchType, String searchName, Pageable pageable) {

        Specification<Team> spec = getTeamSpecification(searchType, searchName);

        return teamRepository.findAll(spec, pageable);
    }

    @Override
    public Team getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow();

        return team;
    }

    @Override
    public Page<Team> getMyTeamList(TeamSearchType searchType, String searchName, Pageable pageable, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Specification<Team> spec = getTeamSpecification(searchType, searchName).and(TeamSpecification.equalUser(user));

        return teamRepository.findAll(spec, pageable);
    }

    private Specification<Team> getTeamSpecification(TeamSearchType searchType, String searchName) {
        Specification<Team> spec = null;

        if(searchType == TeamSearchType.ALL) {
            spec = Specification.where(null);
        }
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
