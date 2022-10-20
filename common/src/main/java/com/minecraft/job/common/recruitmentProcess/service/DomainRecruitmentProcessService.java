package com.minecraft.job.common.recruitmentProcess.service;

import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.domain.RecruitmentRepository;
import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcess;
import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessCreateEvent;
import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DomainRecruitmentProcessService implements RecruitmentProcessService {

    private final UserRepository userRepository;
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
}
