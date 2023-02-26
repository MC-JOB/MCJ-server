package com.minecraft.job.common.recruitmentProcess.service;

import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.domain.RecruitmentRepository;
import com.minecraft.job.common.recruitmentProcess.domain.*;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeRepository;
import com.minecraft.job.common.resume.domain.ResumeSearchType;
import com.minecraft.job.common.resume.domain.ResumeSpecification;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessSearchType.*;
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
    public void inProgress(Long recruitmentProcessId, Long teamId, Long leaderId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        User leader = userRepository.findById(leaderId).orElseThrow();
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcessId).orElseThrow();
        Recruitment recruitment = recruitmentProcess.getRecruitment();

        require(recruitment.ofTeam(team));
        require(team.ofUser(leader));

        recruitmentProcess.inProgress();

        eventPublisher.publishEvent(new RecruitmentProcessInProgressEvent(recruitmentProcess.getId()));
    }

    @Override
    public void pass(Long recruitmentProcessId, Long teamId, Long leaderId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        User leader = userRepository.findById(leaderId).orElseThrow();
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcessId).orElseThrow();
        Recruitment recruitment = recruitmentProcess.getRecruitment();

        require(recruitment.ofTeam(team));
        require(team.ofUser(leader));

        recruitmentProcess.pass();

        eventPublisher.publishEvent(new RecruitmentProcessPassEvent(recruitmentProcess.getId()));
    }

    @Override
    public void cancel(Long recruitmentProcessId, Long teamId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcessId).orElseThrow();
        Recruitment recruitment = recruitmentProcess.getRecruitment();

        require(recruitment.ofTeam(team));
        require(recruitmentProcess.ofUser(user));

        recruitmentProcess.cancel();
    }

    @Override
    public void fail(Long recruitmentProcessId, Long teamId, Long leaderId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        User leader = userRepository.findById(leaderId).orElseThrow();
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcessId).orElseThrow();
        Recruitment recruitment = recruitmentProcess.getRecruitment();

        require(recruitment.ofTeam(team));
        require(team.ofUser(leader));

        recruitmentProcess.fail();

        eventPublisher.publishEvent(new RecruitmentProcessFailEvent(recruitmentProcess.getId()));
    }

    @Override
    public RecruitmentProcess getRecruitmentProcess(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findByUser_Id(userId).orElseThrow();

        require(recruitmentProcess.ofUser(user));

        return recruitmentProcess;
    }

    @Override
    public Page<RecruitmentProcess> getMyRecruitmentProcessList(RecruitmentProcessSearchType searchType, String searchName, Pageable pageable, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Specification<Resume> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user));

        return recruitmentProcessRepository.findAll(spec, pageable);
    }

    private Specification<RecruitmentProcess> getRecruitmentProcessSpecification(RecruitmentProcessSearchType searchType, String searchName) {
        Specification<RecruitmentProcess> spec = null;

        if(searchType == ALL) {
            spec = Specification.where(null);
        }
        if (searchType == USER) {
            User user = userRepository.findByNickname(searchName);
            spec = Specification.where(RecruitmentProcessSpecification.equalUser(user));
        }

        if (searchType == RECRUITMENT) {
            Recruitment recruitment = recruitmentRepository.findByTitle(searchName);
            spec = Specification.where(RecruitmentProcessSpecification.equalRecruitment(recruitment));
        }

        if (searchType == RESUME) {
            Resume resume = resumeRepository.findByTitle(searchName);
            spec = Specification.where(RecruitmentProcessSpecification.equalResume(resume));
        }
        return spec;
    }
}
