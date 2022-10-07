package com.minecraft.job.api.controller.dto;

public class ResumeUpdateDto {

    public record ResumeUpdateRequest(
            Long resumeId, Long userId, String title,
            String content, String trainingHistory
    ) {
    }
}
