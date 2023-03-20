package com.minecraft.job.api.controller.dto.recuritment;

import java.time.LocalDateTime;

public class RecruitmentClosedAtExtendDto {

    public record RecruitmentClosedAtExtendRequest(
            Long recruitmentId, long teamId,
            LocalDateTime closedAt
    ) {
    }
}
