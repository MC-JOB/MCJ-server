package com.minecraft.job.common.recruitmentProcess.service;

import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.domain.RecruitmentRepository;
import com.minecraft.job.common.recruitmentProcess.domain.*;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeRepository;
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
    private final ResumeRepository resumeRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final RecruitmentProcessRepository recruitmentProcessRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public RecruitmentProcess create(Long recruitmentId, Long userId, Long resumeId) {
        User user = userRepository.findById(userId).orElseThrow();
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow();
        Resume resume = resumeRepository.findById(resumeId).orElseThrow();

        RecruitmentProcess recruitmentProcess = RecruitmentProcess.create(recruitment, user, resume);

        recruitmentProcess = recruitmentProcessRepository.save(recruitmentProcess);

        eventPublisher.publishEvent(new RecruitmentProcessCreateEvent(recruitmentProcess.getId()));

        return recruitmentProcess;
    }

    @Override
    public void inProgress(Long recruitmentProcessId, Long teamId, Long teamLeaderId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        User teamLeader = userRepository.findById(teamLeaderId).orElseThrow();
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcessId).orElseThrow();
        Recruitment recruitment = recruitmentProcess.getRecruitment();

        require(recruitment.ofTeam(team));
        require(team.ofUser(teamLeader));

        recruitmentProcess.inProgress();

        eventPublisher.publishEvent(new RecruitmentProcessInProgressEvent(recruitmentProcess.getId()));
    }

    @Override
    public void pass(Long recruitmentProcessId, Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcessId).orElseThrow();
        Recruitment recruitment = recruitmentProcess.getRecruitment();

        require(recruitment.ofTeam(team));

        recruitmentProcess.pass();

        eventPublisher.publishEvent(new RecruitmentProcessPassEvent(recruitmentProcess.getId()));
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

        require(recruitment.ofTeam(team));

        recruitmentProcess.fail();

        eventPublisher.publishEvent(new RecruitmentProcessFailEvent(recruitmentProcess.getId()));
    }
}
