package com.minecraft.job.api.controller.dto;

public class RecruitmentUpdateDto {

    public record RecruitmentUpdateRequest(
            Long recruitmentId, Long userId, Long teamId,
            String title, String content
    ) {
    }
}
