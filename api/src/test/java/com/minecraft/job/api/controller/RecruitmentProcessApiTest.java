package com.minecraft.job.api.controller;

import com.minecraft.job.api.fixture.RecruitmentFixture;
import com.minecraft.job.api.fixture.ResumeFixture;
import com.minecraft.job.api.fixture.TeamFixture;
import com.minecraft.job.api.fixture.UserFixture;
import com.minecraft.job.api.support.ApiTest;
import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.domain.RecruitmentRepository;
import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcess;
import com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessRepository;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeRepository;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.minecraft.job.api.controller.dto.recuritmentprocess.RecruitmentProcessCancelDto.RecruitmentProcessCancelRequest;
import static com.minecraft.job.api.controller.dto.recuritmentprocess.RecruitmentProcessCreateDto.RecruitmentProcessCreateRequest;
import static com.minecraft.job.api.controller.dto.recuritmentprocess.RecruitmentProcessFailDto.RecruitmentProcessFailRequest;
import static com.minecraft.job.api.controller.dto.recuritmentprocess.RecruitmentProcessGetListDto.RecruitmentProcessGetListRequest;
import static com.minecraft.job.api.controller.dto.recuritmentprocess.RecruitmentProcessInProgressDto.RecruitmentProcessInProgressRequest;
import static com.minecraft.job.api.controller.dto.recuritmentprocess.RecruitmentProcessPassDto.RecruitmentProcessPassRequest;
import static com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessSearchType.ALL;
import static com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecruitmentProcessApiTest extends ApiTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private RecruitmentRepository recruitmentRepository;
    @Autowired
    private ResumeRepository resumeRepository;
    @Autowired
    private RecruitmentProcessRepository recruitmentProcessRepository;

    private User user;
    private User leader;
    private Team team;
    private Resume resume;
    private Recruitment recruitment;

    @Test
    @WithUserDetails
    void 채용과정_생성_성공() throws Exception {
        prepareLoggedIn();

        RecruitmentProcessCreateRequest recruitmentProcessCreateRequest = new RecruitmentProcessCreateRequest(recruitment.getId(), resume.getId());

        mockMvc.perform(post("/recruitment-process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recruitmentProcessCreateRequest)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.recruitmentProcess.id").isNotEmpty()
                ).andDo(document("recruitment-process/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @WithUserDetails
    void 채용과정_서류합격_성공() throws Exception {
        prepareLoggedInLeader();

        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.save(RecruitmentProcess.create(recruitment, user, resume));

        RecruitmentProcessInProgressRequest recruitmentProcessInProgressRequest = new RecruitmentProcessInProgressRequest(recruitmentProcess.getId(), team.getId());

        mockMvc.perform(post("/recruitment-process/in-progress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recruitmentProcessInProgressRequest)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("recruitment-process/in-progress",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        RecruitmentProcess findRecruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcess.getId()).orElseThrow();

        assertThat(findRecruitmentProcess.getStatus()).isEqualTo(IN_PROGRESS);
    }

    @Test
    @WithUserDetails
    void 채용과정_최종합격_성공() throws Exception {
        prepareLoggedInLeader();

        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.save(RecruitmentProcess.create(recruitment, user, resume));

        recruitmentProcess.inProgress();

        RecruitmentProcessPassRequest recruitmentProcessPassRequest = new RecruitmentProcessPassRequest(recruitmentProcess.getId(), team.getId());

        mockMvc.perform(post("/recruitment-process/pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recruitmentProcessPassRequest)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("recruitment-process/pass",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        RecruitmentProcess findRecruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcess.getId()).orElseThrow();

        assertThat(findRecruitmentProcess.getStatus()).isEqualTo(PASSED);
    }

    @Test
    @WithUserDetails
    void 채용과정_중도취소_성공() throws Exception {
        prepareLoggedIn();

        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.save(RecruitmentProcess.create(recruitment, user, resume));

        RecruitmentProcessCancelRequest recruitmentProcessCancelRequest = new RecruitmentProcessCancelRequest(recruitmentProcess.getId(), team.getId());

        mockMvc.perform(post("/recruitment-process/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recruitmentProcessCancelRequest)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("recruitment-process/cancel",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        RecruitmentProcess findRecruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcess.getId()).orElseThrow();

        assertThat(findRecruitmentProcess.getStatus()).isEqualTo(CANCELED);
    }

    @Test
    @WithUserDetails
    void 채용과정_불합격_성공() throws Exception {
        prepareLoggedInLeader();

        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.save(RecruitmentProcess.create(recruitment, user, resume));

        RecruitmentProcessFailRequest recruitmentProcessFailRequest = new RecruitmentProcessFailRequest(recruitmentProcess.getId(), team.getId());

        mockMvc.perform(post("/recruitment-process/fail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recruitmentProcessFailRequest)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("recruitment-process/fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        RecruitmentProcess findRecruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcess.getId()).orElseThrow();

        assertThat(findRecruitmentProcess.getStatus()).isEqualTo(FAILED);
    }

    @Test
    @WithUserDetails
    void 채용과정_목록_조회_성공() throws Exception {
        prepareLoggedIn();

        RecruitmentProcessGetListRequest req = new RecruitmentProcessGetListRequest(ALL, "", 0, 10);

        mockMvc.perform(get("/recruitment-process/getMyRecruitmentProcessList")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(status().isOk())
                .andDo(document("recruitment-process/getMyRecruitmentProcessList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Specification<RecruitmentProcess> spec = Specification.where(null);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<RecruitmentProcess> findRecruitmentProcessList = recruitmentProcessRepository.findAll(spec, pageable);

        assertThat(findRecruitmentProcessList).isNotNull();
    }

    @Test
    @WithUserDetails
    void 채용과정_상세_조회_성공() throws Exception {
        prepareLoggedIn();

        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.save(RecruitmentProcess.create(recruitment, user, resume));

        mockMvc.perform(get("/recruitment-process/getMyRecruitmentProcess"))
                .andExpectAll(status().isOk())
                .andDo(document("recruitment-process/getMyRecruitmentProcess",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        RecruitmentProcess findRecruitmentProcess = recruitmentProcessRepository.findByUser_Id(recruitmentProcess.getUser().getId()).orElseThrow();

        assertThat(findRecruitmentProcess).isNotNull();
    }

    private void prepareLoggedInLeader() {
        user = userRepository.save(UserFixture.create());
        leader = prepareLoggedInUser("Leader");
        team = teamRepository.save(TeamFixture.create(leader));
        recruitment = recruitmentRepository.save(RecruitmentFixture.create(team));
        resume = resumeRepository.save(ResumeFixture.create(user));
    }

    private void prepareLoggedIn() {
        user = prepareLoggedInUser("user");
        leader = userRepository.save(UserFixture.create());
        team = teamRepository.save(TeamFixture.create(leader));
        recruitment = recruitmentRepository.save(RecruitmentFixture.create(team));
        resume = resumeRepository.save(ResumeFixture.create(user));
    }
}
