package com.minecraft.job.common.recruitmentProcess.service;

import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcess;

public interface RecruitmentProcessService {

    RecruitmentProcess create(Long recruitmentId, Long userId);
}
