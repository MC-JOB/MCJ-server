package com.minecraft.job.api.controller.dto.resume;

public class ResumeUpdateDto {

    public record ResumeUpdateRequest(
            Long resumeId, String title, String content, String trainingHistory
    ) {
    }
}
