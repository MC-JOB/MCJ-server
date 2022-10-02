package com.minecraft.job.common.recruitment.service;

import com.minecraft.job.common.recruitment.domain.Recruitment;

public interface RecruitmentService {

    Recruitment create(Long userId, Long teamId, String title, String content);
}
