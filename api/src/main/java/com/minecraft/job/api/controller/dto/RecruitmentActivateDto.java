package com.minecraft.job.api.controller.dto;

import java.time.LocalDateTime;

public class RecruitmentActivateDto {

    public record RecruitmentActivateRequest(
            Long recruitmentId, Long userId, Long teamId,
            LocalDateTime closedAt
    ) {
    }
}
