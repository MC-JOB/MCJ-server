package com.minecraft.job.common.recruitmentProcess.service;

import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcess;
import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessSearchType;
import com.minecraft.job.common.resume.domain.ResumeSearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentProcessService {

    RecruitmentProcess create(Long recruitmentId, Long userId, Long resumeId);

    void inProgress(Long recruitmentProcessId, Long teamId, Long leaderId);

    void pass(Long recruitmentProcessId, Long teamId, Long leaderId);

    void cancel(Long recruitmentProcessId, Long teamId, Long userId);

    void fail(Long recruitmentProcessId, Long teamId, Long leaderId);

    RecruitmentProcess getRecruitmentProcess(Long userId);

    Page<RecruitmentProcess> getMyRecruitmentProcessList(RecruitmentProcessSearchType searchType, String searchName, Pageable pageable, Long userId);


}
