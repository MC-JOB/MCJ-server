package com.minecraft.job.api.controller;

import com.minecraft.job.api.security.user.DefaultMcjUser;
import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.minecraft.job.api.controller.dto.RecruitmentGetListDto.*;
import static com.minecraft.job.api.controller.dto.recuritment.RecruitmentActivateDto.RecruitmentActivateRequest;
import static com.minecraft.job.api.controller.dto.recuritment.RecruitmentClosedAtExtendDto.RecruitmentClosedAtExtendRequest;
import static com.minecraft.job.api.controller.dto.recuritment.RecruitmentCreateDto.*;
import static com.minecraft.job.api.controller.dto.recuritment.RecruitmentDeleteDto.RecruitmentDeleteRequest;
import static com.minecraft.job.api.controller.dto.recuritment.RecruitmentGetDetailDto.*;
import static com.minecraft.job.api.controller.dto.recuritment.RecruitmentInactivateDto.RecruitmentInactivateRequest;
import static com.minecraft.job.api.controller.dto.recuritment.RecruitmentUpdateDto.RecruitmentUpdateRequest;

@RestController
@RequestMapping("/recruitment")
@RequiredArgsConstructor
public class RecruitmentApi {

    private final RecruitmentService recruitmentService;

    @PostMapping
    public RecruitmentCreateResponse create(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody RecruitmentCreateRequest req
    ) {
        Recruitment recruitment = recruitmentService.create(user.getId(), req.teamId(), req.title(), req.content());

        return RecruitmentCreateResponse.create(RecruitmentCreateData.create(recruitment));
    }

    @PostMapping("/update")
    public void update(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody RecruitmentUpdateRequest req
    ) {
        recruitmentService.update(req.recruitmentId(), user.getId(), req.teamId(), req.title(), req.content());
    }

    @PostMapping("/inactivate")
    public void inactivate(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody RecruitmentInactivateRequest req
    ) {
        recruitmentService.inactivate(req.recruitmentId(), user.getId(), req.teamId());
    }

    @PostMapping("/activate")
    public void activate(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody RecruitmentActivateRequest req
    ) {
        recruitmentService.activate(req.recruitmentId(), user.getId(), req.teamId(), req.closedAt());
    }

    @PostMapping("/delete")
    public void delete(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody RecruitmentDeleteRequest req
    ) {
        recruitmentService.delete(req.recruitmentId(), user.getId(), req.teamId());
    }

    @PostMapping("/closedAtExtend")
    public void closedAtExtend(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody RecruitmentClosedAtExtendRequest req
    ) {
        recruitmentService.closedAtExtend(req.recruitmentId(), user.getId(), req.teamId(), req.closedAt());
    }

    @GetMapping("/getMyRecruitment")
    public RecruitmentGetDetailResponse getMyRecruitment(
            @RequestBody RecruitmentGetDetailRequest req
    ) {
        Recruitment recruitment = recruitmentService.getRecruitment(req.teamId());

        return RecruitmentGetDetailResponse.getRecruitment(RecruitmentGetDetailData.getRecruitment(recruitment));
    }

    @GetMapping("/getMyRecruitmentList")
    public RecruitmentGetListResponse getMyRecruitmentList(@RequestBody RecruitmentGetListRequest req) {
        PageRequest pageable = PageRequest.of(req.page(), req.size());
        Page<Recruitment> recruitmentList = recruitmentService.getMyRecruitmentList(req.searchType(), req.searchName(), pageable, req.teamId());

        return RecruitmentGetListResponse.getRecruitmentList(RecruitmentGetListData.getRecruitmentList(recruitmentList));
    }
}
