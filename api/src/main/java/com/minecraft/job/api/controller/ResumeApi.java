package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.ResumeCreateDto.ResumeCreateData;
import com.minecraft.job.api.controller.dto.ResumeCreateDto.ResumeCreateRequest;
import com.minecraft.job.api.controller.dto.ResumeCreateDto.ResumeCreateResponse;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
