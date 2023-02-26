package com.minecraft.job.api.controller.dto;

import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeSearchType;
import org.springframework.data.domain.Page;

public class ResumeGetListDto {

    public record ResumeGetListRequest(
            ResumeSearchType searchType,
            String searchName,
            int page,
            int size,
            Long userId
    ) {
    }

    public record ResumeGetListResponse(ResumeGetListData resumes) {

        public static ResumeGetListResponse getResumes(ResumeGetListData resumeGetListData) {
            return new ResumeGetListResponse(resumeGetListData);
        }
    }

    public record ResumeGetListData(Page<Resume> resumes) {

        public static ResumeGetListData getResumes(Page<Resume> resumes) {

            return new ResumeGetListData(resumes);
        }
    }
}
