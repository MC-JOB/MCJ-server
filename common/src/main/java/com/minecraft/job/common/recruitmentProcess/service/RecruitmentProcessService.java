package com.minecraft.job.common.recruitmentProcess.service;

import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcess;

public interface RecruitmentProcessService {

    RecruitmentProcess create(Long recruitmentId, Long userId, Long resumeId);

    void inProgress(Long recruitmentProcessId, Long teamId);

    void pass(Long recruitmentProcessId, Long teamId);

    void cancel(Long recruitmentProcessId, Long teamId);

    void fail(Long recruitmentProcessId, Long teamId);
}
