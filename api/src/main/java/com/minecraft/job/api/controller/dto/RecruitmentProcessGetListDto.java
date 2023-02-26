package com.minecraft.job.api.controller.dto;

import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcess;
import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessSearchType;
import org.springframework.data.domain.Page;

public class RecruitmentProcessGetListDto {
    public record RecruitmentProcessGetListRequest(
            RecruitmentProcessSearchType searchType,
            String searchName,
            int page,
            int size,
            Long userId
    ) {
    }

    public record RecruitmentProcessGetListResponse(RecruitmentProcessGetListData recruitmentProcessGetListData) {
        public static RecruitmentProcessGetListResponse getRecruitmentProcessList(RecruitmentProcessGetListData recruitmentProcessGetListData) {
            return new RecruitmentProcessGetListResponse(recruitmentProcessGetListData);
        }
    }

    public record RecruitmentProcessGetListData(Page<RecruitmentProcess> recruitmentProcesses) {
        public static RecruitmentProcessGetListData getRecruitmentProcessList(Page<RecruitmentProcess> recruitmentProcessesList) {
            return new RecruitmentProcessGetListData(recruitmentProcessesList);
        }
    }
}
