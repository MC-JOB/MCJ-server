package com.minecraft.job.api.controller.dto;

public class RecruitmentProcessInProgressDto {

    public record RecruitmentProcessInProgressRequest(Long recruitmentProcessId, Long teamId) {
    }
}
