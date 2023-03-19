package com.minecraft.job.api.controller.dto.recuritment;

public class RecruitmentInactivateDto {

    public record RecruitmentInactivateRequest(Long recruitmentId, Long teamId) {
    }
}
