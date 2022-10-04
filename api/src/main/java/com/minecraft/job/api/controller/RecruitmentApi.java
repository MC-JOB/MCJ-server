package com.minecraft.job.api.controller;

import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.minecraft.job.api.controller.dto.RecruitmentCreateDto.*;

@RestController
@RequestMapping("/recruitment")
@RequiredArgsConstructor
public class RecruitmentApi {

    private final RecruitmentService recruitmentService;

    @PostMapping
    public RecruitmentCreateResponse create(@RequestBody RecruitmentCreateRequest req) {
        Recruitment recruitment = recruitmentService.create(req.userId(), req.teamId(), req.title(), req.content());

        return RecruitmentCreateResponse.create(RecruitmentCreateData.create(recruitment));
    }
}
