package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.ResumeActivateDto.ResumeActivateRequest;
import com.minecraft.job.api.controller.dto.ResumeCreateDto.ResumeCreateData;
import com.minecraft.job.api.controller.dto.ResumeCreateDto.ResumeCreateRequest;
import com.minecraft.job.api.controller.dto.ResumeCreateDto.ResumeCreateResponse;
import com.minecraft.job.api.controller.dto.ResumeDeleteDto.ResumeDeleteRequest;
import com.minecraft.job.api.controller.dto.ResumeGetListDto.ResumeGetListResponse;
import com.minecraft.job.api.controller.dto.ResumeInactivateDto.ResumeInactivateRequest;
import com.minecraft.job.api.controller.dto.ResumeUpdateDto.ResumeUpdateRequest;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import static com.minecraft.job.api.controller.dto.ResumeGetDetailDto.*;
import static com.minecraft.job.api.controller.dto.ResumeGetListDto.*;

@RestController
@RequestMapping("/resume")
@RequiredArgsConstructor
public class ResumeApi {

    private final ResumeService resumeService;

    @PostMapping
    public ResumeCreateResponse create(@RequestBody ResumeCreateRequest req) {
        Resume resume = resumeService.create(req.userId(), req.title(), req.content(), req.trainingHistory());

        return ResumeCreateResponse.create(ResumeCreateData.create(resume));
    }

    @PostMapping("/update")
    public void update(@RequestBody ResumeUpdateRequest req) {

        resumeService.update(req.resumeId(), req.userId(), req.title(), req.content(), req.trainingHistory());
    }

    @PostMapping("/activate")
    public void activate(@RequestBody ResumeActivateRequest req) {

        resumeService.activate(req.resumeId(), req.userId());
    }

    @PostMapping("/inactivate")
    public void inactivate(@RequestBody ResumeInactivateRequest req) {

        resumeService.inactivate(req.resumeId(), req.userId());
    }

    @PostMapping("/delete")
    public void delete(@RequestBody ResumeDeleteRequest req) {

        resumeService.delete(req.resumeId(), req.userId());
    }

    @GetMapping("/getMyResumes")
    public ResumeGetListResponse getMyResumes(@RequestBody ResumeGetListRequest req) {
        PageRequest pageable = PageRequest.of(req.page(), req.size());
        Page<Resume> resumes = resumeService.getMyResumes(req.searchType(), req.searchName(), pageable, req.userId());

        return ResumeGetListResponse.getResumes(ResumeGetListData.getResumes(resumes));
    }

    @GetMapping("/getMyResume")
    public ResumeGetDetailResponse getMyResumeDetail(@RequestBody ResumeGetDetailRequest req) {
        Resume resume = resumeService.getResume(req.userId());
        
        return ResumeGetDetailResponse.getResume(ResumeGetDetailData.getResume(resume));
    }
}
