package com.minecraft.job.common.recruitmentProcess.service;

import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcess;

public interface RecruitmentProcessService {

    RecruitmentProcess create(Long recruitmentId, Long userId);

    void inProgress(Long recruitmentProcessId, Long userId, Long teamId);

    void cancel(Long recruitmentProcessId, Long userId, Long teamId);

    void fail(Long recruitmentProcessId, Long userId, Long teamId);
}
