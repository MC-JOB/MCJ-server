package com.minecraft.job.api.controller.dto;

public class RecruitmentProcessCancelDto {

    public record RecruitmentProcessCancelRequest(Long recruitmentProcessId, Long teamId) {
    }
}
