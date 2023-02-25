package com.minecraft.job.api.controller.dto;

import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeSearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ResumeGetResumesDto {

    public record ResumeGetResumesRequest(
            ResumeSearchType searchType,
            String searchName,
            int page,
            int size,
            String userNickName
    ) {
    }

    public record ResumeGetResumesResponse(ResumeGetResumesData resumes){

        public static ResumeGetResumesResponse getResumes(ResumeGetResumesData resumeGetResumesData) {
            return new ResumeGetResumesResponse(resumeGetResumesData);
        }
    }

    public record ResumeGetResumesData(Page<Resume> resumes) {

        public static ResumeGetResumesData getResumes(Page<Resume> resumes) {

            return new ResumeGetResumesData(resumes);
        }
    }
}
