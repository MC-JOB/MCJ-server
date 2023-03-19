package com.minecraft.job.api.controller.dto;

import java.time.LocalDateTime;

public class RecruitmentActivateDto {

    public record RecruitmentActivateRequest(
            Long recruitmentId, Long teamId,
            LocalDateTime closedAt
    ) {
    }
}
