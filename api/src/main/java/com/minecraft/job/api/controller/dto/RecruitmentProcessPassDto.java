package com.minecraft.job.api.controller.dto;

public class RecruitmentProcessPassDto {

    public record RecruitmentProcessPassRequest(Long recruitmentProcessId, Long teamId, Long leaderId) {
    }
}
