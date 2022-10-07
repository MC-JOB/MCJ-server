package com.minecraft.job.api.controller.dto;

public class ResumeDeleteDto {

    public record ResumeDeleteRequest(Long resumeId, Long userId) {
    }
}
