package com.minecraft.job.api.controller.dto.recuritment;

import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.domain.RecruitmentStatus;

import java.time.LocalDateTime;

public class RecruitmentGetDetailDto {

    public record RecruitmentGetDetailRequest(
            Long teamId
    ) {
    }

    public record RecruitmentGetDetailResponse(
            RecruitmentGetDetailData Recruitment
    ) {
        public static RecruitmentGetDetailResponse getRecruitment(RecruitmentGetDetailData recruitmentGetDetailData) {
            return new RecruitmentGetDetailResponse(recruitmentGetDetailData);
        }
    }

    public record RecruitmentGetDetailData(
            Long id, String title, String content,
            RecruitmentStatus status, LocalDateTime createdAt
    ) {
        public static RecruitmentGetDetailData getRecruitment(Recruitment recruitment) {
            return new RecruitmentGetDetailData(
                    recruitment.getId(), recruitment.getTitle(), recruitment.getContent(),
                    recruitment.getStatus(), recruitment.getCreatedAt()
            );
        }
    }
}
