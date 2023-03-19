package com.minecraft.job.api.controller.dto.recuritment;

public class RecruitmentDeleteDto {

    public record RecruitmentDeleteRequest(Long recruitmentId, Long teamId) {
    }
}
