package com.minecraft.job.api.controller.dto;

public class RecruitmentInactivateDto {

    public record RecruitmentInactivateRequest(Long recruitmentId, Long userId, Long teamId) {
    }
}
