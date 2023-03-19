package com.minecraft.job.api.controller.dto;

import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeSearchType;
import org.springframework.data.domain.Page;

public class ResumeGetListDto {

    public record ResumeGetListRequest(
            ResumeSearchType searchType,
            String searchName,
            int page,
            int size
    ) {
    }

    public record ResumeGetListResponse(ResumeGetListData resumeList) {

        public static ResumeGetListResponse getResumeList(ResumeGetListData resumeGetListData) {
            return new ResumeGetListResponse(resumeGetListData);
        }
    }

    public record ResumeGetListData(Page<Resume> resumeList) {

        public static ResumeGetListData getResumeList(Page<Resume> resumeList) {

            return new ResumeGetListData(resumeList);
        }
    }
}
