package com.minecraft.job.api.controller.dto;

public class RecruitmentDeleteDto {

    public record RecruitmentDeleteRequest(Long recruitmentId, Long userId, Long teamId) {
    }
}
