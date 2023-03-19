package com.minecraft.job.api.controller.dto;

import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeStatus;

import java.time.LocalDateTime;

public class ResumeGetDetailDto {

    public record ResumeGetDetailResponse(
            ResumeGetDetailData resume
    ) {
        public static ResumeGetDetailResponse getResume(ResumeGetDetailData resumeGetDetailData) {
            return new ResumeGetDetailResponse(resumeGetDetailData);
        }
    }

    public record ResumeGetDetailData(
            Long id, String title, String content, String trainingHistory,
            ResumeStatus status, LocalDateTime createdAt
    ) {
        public static ResumeGetDetailData getResume(Resume resume) {
            return new ResumeGetDetailData(
                    resume.getId(), resume.getTitle(), resume.getContent(), resume.getTrainingHistory(),
                    resume.getStatus(), resume.getCreatedAt()
            );
        }
    }
}
