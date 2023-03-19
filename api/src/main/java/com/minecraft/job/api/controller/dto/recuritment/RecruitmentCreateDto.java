package com.minecraft.job.api.controller.dto.recuritment;

import com.minecraft.job.common.recruitment.domain.Recruitment;

public class RecruitmentCreateDto {

    public record RecruitmentCreateRequest(Long teamId, String title, String content) {
    }

    public record RecruitmentCreateResponse(RecruitmentCreateData recruitment) {

        public static RecruitmentCreateResponse create(RecruitmentCreateData recruitment) {
            return new RecruitmentCreateResponse(recruitment);
        }
    }

    public record RecruitmentCreateData(Long id, String title) {

        public static RecruitmentCreateData create(Recruitment recruitment) {
            return new RecruitmentCreateData(recruitment.getId(), recruitment.getTitle());
        }
    }
}
