package com.minecraft.job.common.resume.service;

import com.minecraft.job.common.resume.domain.Resume;

public interface ResumeService {

    Resume create(Long userId, String title, String content, String trainingHistory);

    void update(Long resumeId, Long userId, String title, String content, String trainingHistory);
}
