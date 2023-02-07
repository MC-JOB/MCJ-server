package com.minecraft.job.common.resume.service;

import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeSearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResumeService {

    Resume create(Long userId, String title, String content, String trainingHistory);

    void update(Long resumeId, Long userId, String title, String content, String trainingHistory);

    void activate(Long resumeId, Long userId);

    void inactivate(Long resumeId, Long userId);

    void delete(Long resumeId, Long userId);

    Page<Resume> getResumes(ResumeSearchType searchType, String searchName, Pageable pageable);
}
