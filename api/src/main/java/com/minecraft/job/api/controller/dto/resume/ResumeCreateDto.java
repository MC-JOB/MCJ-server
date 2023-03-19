package com.minecraft.job.api.controller.dto.resume;

import com.minecraft.job.common.resume.domain.Resume;

public class ResumeCreateDto {

    public record ResumeCreateRequest(
            String title, String content, String trainingHistory
    ) {
    }

    public record ResumeCreateResponse(ResumeCreateData resume) {

        public static ResumeCreateResponse create(ResumeCreateData resumeCreateData) {
            return new ResumeCreateResponse(resumeCreateData);
        }
    }

    public record ResumeCreateData(Long id, String title) {

        public static ResumeCreateData create(Resume resume) {
            return new ResumeCreateData(resume.getId(), resume.getTitle());
        }
    }
}
