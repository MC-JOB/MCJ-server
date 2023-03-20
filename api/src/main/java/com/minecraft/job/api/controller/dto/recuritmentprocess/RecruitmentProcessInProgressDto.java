package com.minecraft.job.api.controller.dto.recuritmentprocess;

public class RecruitmentProcessInProgressDto {

    public record RecruitmentProcessInProgressRequest(Long recruitmentProcessId, Long teamId) {
    }
}
