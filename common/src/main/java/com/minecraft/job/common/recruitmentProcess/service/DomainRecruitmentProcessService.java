package com.minecraft.job.common.recruitmentProcess.service;

import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.domain.RecruitmentRepository;
import com.minecraft.job.common.recruitmentProcess.domain.*;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.minecraft.job.common.support.Preconditions.require;

@Service
@Transactional
@RequiredArgsConstructor
public class DomainRecruitmentProcessService implements RecruitmentProcessService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final RecruitmentProcessRepository recruitmentProcessRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public RecruitmentProcess create(Long recruitmentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow();

        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user);

        recruitmentProcess = recruitmentProcessRepository.save(recruitmentProcess);

        eventPublisher.publishEvent(new RecruitmentProcessCreateEvent(recruitmentProcess.getId()));

        return recruitmentProcess;
    }

    @Override
    public void inProgress(Long recruitmentProcessId, Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcessId).orElseThrow();
        Recruitment recruitment = recruitmentProcess.getRecruitment();

        eventPublisher.publishEvent(new RecruitmentProcessInProgressEvent(recruitmentProcess.getId()));

        require(recruitment.ofTeam(team));

        recruitmentProcess.inProgress();
    }

    @Override
    public void pass(Long recruitmentProcessId, Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcessId).orElseThrow();
        Recruitment recruitment = recruitmentProcess.getRecruitment();

        eventPublisher.publishEvent(new RecruitmentProcessPassEvent(recruitmentProcess.getId()));

        require(recruitment.ofTeam(team));

        recruitmentProcess.pass();
    }

    @Override
    public void cancel(Long recruitmentProcessId, Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcessId).orElseThrow();
        Recruitment recruitment = recruitmentProcess.getRecruitment();

        require(recruitment.ofTeam(team));

        recruitmentProcess.cancel();
    }

    @Override
    public void fail(Long recruitmentProcessId, Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcessId).orElseThrow();
        Recruitment recruitment = recruitmentProcess.getRecruitment();

        eventPublisher.publishEvent(new RecruitmentProcessFailEvent(recruitmentProcess.getId()));

        require(recruitment.ofTeam(team));

        recruitmentProcess.fail();
    }
}
