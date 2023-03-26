package com.minecraft.job.common.recruitment.service;

import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.domain.RecruitmentSearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface RecruitmentService {

    Recruitment create(Long userId, Long teamId, String title, String content);

    void activate(Long recruitmentId, Long userId, Long teamId, LocalDateTime closedAt);

    void inactivate(Long recruitmentId, Long userId, long teamId);

    void delete(Long recruitmentId, Long userId, long teamId);

    void update(Long recruitmentId, Long userId, long teamId, String title, String content);

    void closedAtExtend(Long recruitmentId, Long userId, long teamId, LocalDateTime closedAt);

    Page<Recruitment> getRecruitments(RecruitmentSearchType searchType, String searchName, Pageable pageable);

    Recruitment getRecruitment(Long teamId);

    Page<Recruitment> getMyRecruitmentList(RecruitmentSearchType searchType, String searchName, Pageable pageable, Long teamId);
}
