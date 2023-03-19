package com.minecraft.job.api.controller.dto;

import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcess;

public class RecruitmentProcessCreateDto {

    public record RecruitmentProcessCreateRequest(Long recruitmentId, Long resumeId) {
    }

    public record RecruitmentProcessResponse(RecruitmentProcessCreateData recruitmentProcess) {

        public static RecruitmentProcessResponse create(RecruitmentProcessCreateData recruitmentProcess) {
            return new RecruitmentProcessResponse(recruitmentProcess);
        }
    }

    public record RecruitmentProcessCreateData(Long id) {

        public static RecruitmentProcessCreateData create(RecruitmentProcess recruitmentProcess) {
            return new RecruitmentProcessCreateData(recruitmentProcess.getId());
        }
    }
}
