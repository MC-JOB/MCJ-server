package com.minecraft.job.api.controller.dto;

public class ResumeUpdateDto {

    public record ResumeUpdateRequest(
            Long resumeId, String title, String content, String trainingHistory
    ) {
    }
}
