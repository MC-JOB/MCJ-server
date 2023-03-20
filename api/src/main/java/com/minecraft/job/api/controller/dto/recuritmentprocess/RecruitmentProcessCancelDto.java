package com.minecraft.job.api.controller.dto.recuritmentprocess;

public class RecruitmentProcessCancelDto {

    public record RecruitmentProcessCancelRequest(Long recruitmentProcessId, Long teamId) {
    }
}
