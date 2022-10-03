package com.minecraft.job.common.recruitment.service;

import com.minecraft.job.common.recruitment.domain.Recruitment;

import java.time.LocalDateTime;

public interface RecruitmentService {

    Recruitment create(Long userId, Long teamId, String title, String content);

    void activate(Long recruitmentId, Long userId, Long teamId, LocalDateTime closedAt);

    void inactivate(Long recruitmentId, Long userId, long teamId);

    void delete(Long recruitmentId, Long userId, long teamId);

    void update(Long recruitmentId, Long userId, long teamId, String title, String content);
}
