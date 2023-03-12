package com.minecraft.job.api.controller;

import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.minecraft.job.api.controller.dto.RecruitmentActivateDto.RecruitmentActivateRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentClosedAtExtendDto.RecruitmentClosedAtExtendRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentCreateDto.*;
import static com.minecraft.job.api.controller.dto.RecruitmentDeleteDto.RecruitmentDeleteRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentGetDetailDto.*;
import static com.minecraft.job.api.controller.dto.RecruitmentInactivateDto.RecruitmentInactivateRequest;
import static com.minecraft.job.api.controller.dto.RecruitmentUpdateDto.RecruitmentUpdateRequest;

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

    @PostMapping("/update")
    public void update(@RequestBody RecruitmentUpdateRequest req) {

        recruitmentService.update(req.recruitmentId(), req.userId(), req.teamId(), req.title(), req.content());
    }

    @PostMapping("/inactivate")
    public void inactivate(@RequestBody RecruitmentInactivateRequest req) {

        recruitmentService.inactivate(req.recruitmentId(), req.userId(), req.teamId());
    }

    @PostMapping("/activate")
    public void activate(@RequestBody RecruitmentActivateRequest req) {

        recruitmentService.activate(req.recruitmentId(), req.userId(), req.teamId(), req.closedAt());
    }

    @PostMapping("/delete")
    public void delete(@RequestBody RecruitmentDeleteRequest req) {

        recruitmentService.delete(req.recruitmentId(), req.userId(), req.teamId());
    }

    @PostMapping("/closedAtExtend")
    public void closedAtExtend(@RequestBody RecruitmentClosedAtExtendRequest req) {

        recruitmentService.closedAtExtend(req.recruitmentId(), req.userId(), req.teamId(), req.closedAt());
    }

    @GetMapping("/getMyRecruitment")
    public RecruitmentGetDetailResponse getMyRecruitment(@RequestBody RecruitmentGetDetailRequest req) {

        Recruitment recruitment = recruitmentService.getRecruitment(req.teamId());

        return RecruitmentGetDetailResponse.getRecruitment(RecruitmentGetDetailData.getRecruitment(recruitment));
    }
}
