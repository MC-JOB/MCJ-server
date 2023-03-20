package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.resume.ResumeActivateDto.ResumeActivateRequest;
import com.minecraft.job.api.controller.dto.resume.ResumeCreateDto.ResumeCreateData;
import com.minecraft.job.api.controller.dto.resume.ResumeCreateDto.ResumeCreateRequest;
import com.minecraft.job.api.controller.dto.resume.ResumeCreateDto.ResumeCreateResponse;
import com.minecraft.job.api.controller.dto.resume.ResumeDeleteDto.ResumeDeleteRequest;
import com.minecraft.job.api.controller.dto.resume.ResumeGetListDto.ResumeGetListResponse;
import com.minecraft.job.api.controller.dto.resume.ResumeInactivateDto.ResumeInactivateRequest;
import com.minecraft.job.api.controller.dto.resume.ResumeUpdateDto.ResumeUpdateRequest;
import com.minecraft.job.api.security.user.DefaultMcjUser;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.minecraft.job.api.controller.dto.resume.ResumeGetDetailDto.ResumeGetDetailData;
import static com.minecraft.job.api.controller.dto.resume.ResumeGetDetailDto.ResumeGetDetailResponse;
import static com.minecraft.job.api.controller.dto.resume.ResumeGetListDto.ResumeGetListData;
import static com.minecraft.job.api.controller.dto.resume.ResumeGetListDto.ResumeGetListRequest;

@RestController
@RequestMapping("/resume")
@RequiredArgsConstructor
public class ResumeApi {

    private final ResumeService resumeService;

    @PostMapping
    public ResumeCreateResponse create(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody ResumeCreateRequest req
    ) {
        Resume resume = resumeService.create(user.getId(), req.title(), req.content(), req.trainingHistory());

        return ResumeCreateResponse.create(ResumeCreateData.create(resume));
    }

    @PostMapping("/update")
    public void update(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody ResumeUpdateRequest req
    ) {
        resumeService.update(req.resumeId(), user.getId(), req.title(), req.content(), req.trainingHistory());
    }

    @PostMapping("/activate")
    public void activate(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody ResumeActivateRequest req
    ) {
        resumeService.activate(req.resumeId(), user.getId());
    }

    @PostMapping("/inactivate")
    public void inactivate(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody ResumeInactivateRequest req
    ) {
        resumeService.inactivate(req.resumeId(), user.getId());
    }

    @PostMapping("/delete")
    public void delete(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody ResumeDeleteRequest req
    ) {
        resumeService.delete(req.resumeId(), user.getId());
    }

    @GetMapping("/getMyResumeList")
    public ResumeGetListResponse getMyResumeList(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody ResumeGetListRequest req
    ) {
        PageRequest pageable = PageRequest.of(req.page(), req.size());

        Page<Resume> resumeList = resumeService.getMyResumeList(req.searchType(), req.searchName(), pageable, user.getId());

        return ResumeGetListResponse.getResumeList(ResumeGetListData.getResumeList(resumeList));
    }

    @GetMapping("/getMyResume")
    public ResumeGetDetailResponse getMyResumeDetail(
            @AuthenticationPrincipal DefaultMcjUser user
    ) {
        Resume resume = resumeService.getResume(user.getId());

        return ResumeGetDetailResponse.getResume(ResumeGetDetailData.getResume(resume));
    }
}
