package com.minecraft.job.api.controller.dto;

import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcess;
import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessStatus;

import java.time.LocalDateTime;

public class RecruitmentProcessGetDetailDto {
    public record RecruitmentProcessGetDetailRequest(
            Long userId
    ) {
    }

    public record RecruitmentProcessGetDetailResponse(
            RecruitmentProcessGetDetailData recruitmentProcess
    ) {
        public static RecruitmentProcessGetDetailResponse getRecruitmentProcess(RecruitmentProcessGetDetailData recruitmentProcessGetDetailData) {
            return new RecruitmentProcessGetDetailResponse(recruitmentProcessGetDetailData);
        }
    }

    public record RecruitmentProcessGetDetailData(
            Long id, LocalDateTime closedAt, LocalDateTime createdAt, RecruitmentProcessStatus status
    ) {
        public static RecruitmentProcessGetDetailData getRecruitmentProcess(RecruitmentProcess recruitmentProcess) {
            return new RecruitmentProcessGetDetailData(
                    recruitmentProcess.getId(), recruitmentProcess.getClosedAt(),
                    recruitmentProcess.getCreatedAt(), recruitmentProcess.getStatus()
            );
        }
    }
}
