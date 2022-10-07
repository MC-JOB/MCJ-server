package com.minecraft.job.api.controller.dto;

import java.time.LocalDateTime;

public class RecruitmentClosedAtExtendDto {

    public record RecruitmentClosedAtExtendRequest(
            Long recruitmentId, Long userId, long teamId,
            LocalDateTime closedAt
    ) {
    }
}
