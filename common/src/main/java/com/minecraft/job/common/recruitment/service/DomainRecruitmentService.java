package com.minecraft.job.common.recruitment.service;

import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.domain.RecruitmentRepository;
import com.minecraft.job.common.recruitment.domain.RecruitmentSearchType;
import com.minecraft.job.common.recruitment.domain.RecruitmentSpecification;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.minecraft.job.common.support.Preconditions.require;

@Service
@Transactional
@RequiredArgsConstructor
public class DomainRecruitmentService implements RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;

    private final TeamRepository teamRepository;

    private final UserRepository userRepository;

    @Override
    public Recruitment create(Long userId, Long teamId, String title, String content) {
        User user = userRepository.findById(userId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();

        require(team.ofUser(user));

        Recruitment recruitment = Recruitment.create(title, content, team);

        return recruitmentRepository.save(recruitment);
    }

    @Override
    public void activate(Long recruitmentId, Long userId, Long teamId, LocalDateTime closedAt) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();

        require(team.ofUser(user));
        require(recruitment.ofTeam(team));

        recruitment.activate(closedAt);
    }

    @Override
    public void inactivate(Long recruitmentId, Long userId, long teamId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();

        require(team.ofUser(user));
        require(recruitment.ofTeam(team));

        recruitment.inactivate();
    }

    @Override
    public void delete(Long recruitmentId, Long userId, long teamId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();

        require(team.ofUser(user));
        require(recruitment.ofTeam(team));

        recruitment.delete();
    }

    @Override
    public void update(Long recruitmentId, Long userId, long teamId, String title, String content) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();

        require(team.ofUser(user));
        require(recruitment.ofTeam(team));

        recruitment.update(title, content);
    }

    @Override
    public void closedAtExtend(Long recruitmentId, Long userId, long teamId, LocalDateTime closedAt) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();

        require(team.ofUser(user));
        require(recruitment.ofTeam(team));

        recruitment.closedAtExtend(closedAt);
    }

    @Override
    public Page<Recruitment> getRecruitments(RecruitmentSearchType searchType, String searchName, int page) {

        Specification<Recruitment> spec = getRecruitmentSpecification(searchType, searchName);

        PageRequest pageRequest = PageRequest.of(page, 10);

        return recruitmentRepository.findAll(spec, pageRequest);
    }

    private Specification<Recruitment> getRecruitmentSpecification(RecruitmentSearchType searchType, String searchName) {
        Specification<Recruitment> spec = null;

        if (searchType == RecruitmentSearchType.TITLE) {
            spec = Specification.where(RecruitmentSpecification.likeTitle(searchName));
        }
        if (searchType == RecruitmentSearchType.CONTENT) {
            spec = Specification.where(RecruitmentSpecification.likeContent(searchName));
        }
        if (searchType == RecruitmentSearchType.TEAM) {
            Team team = teamRepository.findByName(searchName);
            spec = Specification.where(RecruitmentSpecification.equalTeam(team));
        }
        return spec;
    }
}
