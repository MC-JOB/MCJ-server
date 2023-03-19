package com.minecraft.job.api.controller;

import com.minecraft.job.api.security.user.DefaultMcjUser;
import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcess;
import com.minecraft.job.common.recruitmentProcess.service.RecruitmentProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.minecraft.job.api.controller.dto.RecruitmentProcessCancelDto.RecruitmentProcessCancelRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessCreateDto.*;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessFailDto.RecruitmentProcessFailRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessGetDetailDto.RecruitmentProcessGetDetailData;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessGetDetailDto.RecruitmentProcessGetDetailResponse;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessGetListDto.*;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessInProgressDto.RecruitmentProcessInProgressRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentProcessPassDto.RecruitmentProcessPassRequest;

@RestController
@RequestMapping("/recruitment-process")
@RequiredArgsConstructor
public class RecruitmentProcessApi {

    private final RecruitmentProcessService recruitmentProcessService;

    @PostMapping
    public RecruitmentProcessResponse create(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody RecruitmentProcessCreateRequest req
    ) {
        RecruitmentProcess recruitmentProcess = recruitmentProcessService.create(req.recruitmentId(), user.getId(), req.resumeId());

        return RecruitmentProcessResponse.create(RecruitmentProcessCreateData.create(recruitmentProcess));
    }

    @PostMapping("/in-progress")
    public void inProgress(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody RecruitmentProcessInProgressRequest req
    ) {
        recruitmentProcessService.inProgress(req.recruitmentProcessId(), req.teamId(), user.getId());
    }

    @PostMapping("/pass")
    public void pass(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody RecruitmentProcessPassRequest req
    ) {
        recruitmentProcessService.pass(req.recruitmentProcessId(), req.teamId(), user.getId());
    }

    @PostMapping("/cancel")
    public void cancel(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody RecruitmentProcessCancelRequest req
    ) {
        recruitmentProcessService.cancel(req.recruitmentProcessId(), req.teamId(), user.getId());
    }

    @PostMapping("/fail")
    public void fail(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody RecruitmentProcessFailRequest req
    ) {
        recruitmentProcessService.fail(req.recruitmentProcessId(), req.teamId(), user.getId());
    }

    @GetMapping("/getMyRecruitmentProcessList")
    public RecruitmentProcessGetListResponse getMyRecruitmentProcessList(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody RecruitmentProcessGetListRequest req
    ) {
        PageRequest pageable = PageRequest.of(req.page(), req.size());

        Page<RecruitmentProcess> recruitmentProcessList = recruitmentProcessService.getMyRecruitmentProcessList(req.searchType(), req.searchName(), pageable, user.getId());

        return RecruitmentProcessGetListResponse.getRecruitmentProcessList(RecruitmentProcessGetListData.getRecruitmentProcessList(recruitmentProcessList));
    }

    @GetMapping("/getMyRecruitmentProcess")
    public RecruitmentProcessGetDetailResponse getMyRecruitmentProcessDetail(
            @AuthenticationPrincipal DefaultMcjUser user
    ) {
        RecruitmentProcess recruitmentProcess = recruitmentProcessService.getRecruitmentProcess(user.getId());

        return RecruitmentProcessGetDetailResponse.getRecruitmentProcess(RecruitmentProcessGetDetailData.getRecruitmentProcess(recruitmentProcess));
    }
}
