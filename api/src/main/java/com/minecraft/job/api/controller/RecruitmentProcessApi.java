package com.minecraft.job.api.controller;

import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcess;
import com.minecraft.job.common.recruitmentProcess.service.RecruitmentProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.minecraft.job.api.controller.dto.RecruitmentProcessCancelDto.RecruitmentProcessCancelRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessCreateDto.*;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessInProgressDto.RecruitmentProcessInProgressRequest;

@RestController
@RequestMapping("/recruitment-process")
@RequiredArgsConstructor
public class RecruitmentProcessApi {

    private final RecruitmentProcessService recruitmentProcessService;

    @PostMapping
    public RecruitmentProcessResponse create(@RequestBody RecruitmentProcessCreateRequest req) {
        RecruitmentProcess recruitmentProcess = recruitmentProcessService.create(req.recruitmentId(), req.userId(), req.resumeId());

        return RecruitmentProcessResponse.create(RecruitmentProcessCreateData.create(recruitmentProcess));
    }

    @PostMapping("/in-progress")
    public void inProgress(@RequestBody RecruitmentProcessInProgressRequest req) {
        recruitmentProcessService.inProgress(req.recruitmentProcessId(), req.teamId(), req.leaderId());
    }

    @PostMapping("/cancel")
    public void cancel(@RequestBody RecruitmentProcessCancelRequest req) {
        recruitmentProcessService.cancel(req.recruitmentProcessId(), req.teamId(), req.leaderId());
    }
}
