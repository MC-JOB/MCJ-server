package com.minecraft.job.api.controller;

import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcess;
import com.minecraft.job.common.recruitmentProcess.service.RecruitmentProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import static com.minecraft.job.api.controller.dto.RecruitmentProcessCancelDto.RecruitmentProcessCancelRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessCreateDto.*;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessFailDto.RecruitmentProcessFailRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessGetDetailDto.*;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessGetListDto.*;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessInProgressDto.RecruitmentProcessInProgressRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessPassDto.RecruitmentProcessPassRequest;

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

    @PostMapping("/pass")
    public void pass(@RequestBody RecruitmentProcessPassRequest req) {
        recruitmentProcessService.pass(req.recruitmentProcessId(), req.teamId(), req.leaderId());
    }

    @PostMapping("/cancel")
    public void cancel(@RequestBody RecruitmentProcessCancelRequest req) {
        recruitmentProcessService.cancel(req.recruitmentProcessId(), req.teamId(), req.userId());
    }

    @PostMapping("/fail")
    public void fail(@RequestBody RecruitmentProcessFailRequest req) {
        recruitmentProcessService.fail(req.recruitmentProcessId(), req.teamId(), req.leaderId());
    }

    @GetMapping("/getMyRecruitmentProcessList")
    public RecruitmentProcessGetListResponse getMyRecruitmentProcessList(@RequestBody RecruitmentProcessGetListRequest req) {
        PageRequest pageable = PageRequest.of(req.page(), req.size());
        Page<RecruitmentProcess> recruitmentProcessList = recruitmentProcessService.getMyRecruitmentProcessList(req.searchType(), req.searchName(), pageable, req.userId());

        return RecruitmentProcessGetListResponse.getRecruitmentProcessList(RecruitmentProcessGetListData.getRecruitmentProcessList(recruitmentProcessList));
    }

    @GetMapping("/getMyRecruitmentProcess")
    public RecruitmentProcessGetDetailResponse getMyRecruitmentProcessDetail(@RequestBody RecruitmentProcessGetDetailRequest req) {
        RecruitmentProcess recruitmentProcess = recruitmentProcessService.getRecruitmentProcess(req.userId());

        return RecruitmentProcessGetDetailResponse.getRecruitmentProcess(RecruitmentProcessGetDetailData.getRecruitmentProcess(recruitmentProcess));
    }
}
