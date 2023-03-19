package com.minecraft.job.api.controller.dto;

public class RecruitmentUpdateDto {

    public record RecruitmentUpdateRequest(
            Long recruitmentId, Long teamId,
            String title, String content
    ) {
    }
}
