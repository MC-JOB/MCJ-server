package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.ResumeGetListDto;
import com.minecraft.job.api.controller.dto.ReviewActivateDto.ReviewActivateRequest;
import com.minecraft.job.api.controller.dto.ReviewCreateDto.ReviewCreateRequest;
import com.minecraft.job.api.controller.dto.ReviewGetListDto;
import com.minecraft.job.api.controller.dto.ReviewInactivateDto.ReviewInactivateRequest;
import com.minecraft.job.api.controller.dto.ReviewUpdateDto.ReviewUpdateRequest;
import com.minecraft.job.api.fixture.ReviewFixture;
import com.minecraft.job.api.fixture.TeamFixture;
import com.minecraft.job.api.fixture.UserFixture;
import com.minecraft.job.api.support.ApiTest;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.review.domain.Review;
import com.minecraft.job.common.review.domain.ReviewRepository;
import com.minecraft.job.common.review.domain.ReviewSearchType;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.minecraft.job.api.controller.dto.ReviewGetListDto.*;
import static com.minecraft.job.common.resume.domain.ResumeSearchType.ALL;
import static com.minecraft.job.common.review.domain.ReviewSearchType.*;
import static com.minecraft.job.common.review.domain.ReviewStatus.ACTIVATED;
import static com.minecraft.job.common.review.domain.ReviewStatus.INACTIVATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewApiTest extends ApiTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    private User user;
    private Team team;

    @BeforeEach
    void setUp() {
        user = prepareLoggedInUser("setUp");
        User leader = userRepository.save(UserFixture.getAnotherUser("leader"));
        team = teamRepository.save(TeamFixture.create(leader));
    }

    @Test
    @WithUserDetails
    void 리뷰_생성_성공() throws Exception {
        ReviewCreateRequest req = new ReviewCreateRequest(user.getId(), team.getId(), "content", 3L);

        mockMvc.perform(post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.review.id").isNotEmpty(),
                        jsonPath("$.review.score").value(3L),
                        jsonPath("$.averagePoint").value(3L)
                ).andDo(document("review/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getAveragePoint()).isEqualTo(3L);
    }

    @Test
    @WithUserDetails
    void 리뷰_수정_성공() throws Exception {
        Review review = reviewRepository.save(ReviewFixture.create(user, team));

        ReviewUpdateRequest req = new ReviewUpdateRequest(review.getId(), user.getId(), team.getId(), "updateContent", 1L);

        mockMvc.perform(post("/review/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("review/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Review findReview = reviewRepository.findById(review.getId()).orElseThrow();
        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findReview.getContent()).isEqualTo("updateContent");
        assertThat(findReview.getScore()).isEqualTo(1L);
        assertThat(findTeam.getAveragePoint()).isEqualTo(1L);
    }

    @Test
    @WithUserDetails
    void 리뷰_활성화_성공() throws Exception {
        Review review = ReviewFixture.create(user, team);
        review.inactivate();
        review = reviewRepository.save(review);

        ReviewActivateRequest req = new ReviewActivateRequest(review.getId(), user.getId(), team.getId(), "content", 1L);

        mockMvc.perform(post("/review/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("review/activate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Review findReview = reviewRepository.findById(review.getId()).orElseThrow();
        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findReview.getStatus()).isEqualTo(ACTIVATED);
        assertThat(findReview.getContent()).isEqualTo("content");
        assertThat(findReview.getScore()).isEqualTo(1L);
        assertThat(findTeam.getAveragePoint()).isEqualTo(1L);
    }

    @Test
    @WithUserDetails
    void 리뷰_비활성화_성공() throws Exception {
        Review review = reviewRepository.save(ReviewFixture.create(user, team));

        ReviewInactivateRequest req = new ReviewInactivateRequest(review.getId(), user.getId(), team.getId());
        mockMvc.perform(post("/review/inactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("review/inactivate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Review findReview = reviewRepository.findById(review.getId()).orElseThrow();
        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findReview.getStatus()).isEqualTo(INACTIVATED);
        assertThat(findTeam.getAveragePoint()).isEqualTo(0);
    }

    @Test
    @WithUserDetails
    void 리뷰_목록_조회_성공() throws Exception {
        ReviewGetListRequest req = new ReviewGetListRequest(ReviewSearchType.ALL, "", 0, 10);

        mockMvc.perform(get("/review/getMyReviewList")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(status().isOk())
                .andDo(document("review/getMyReviewList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Specification<Resume> spec = Specification.where(null);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Review> findReviewList = reviewRepository.findAll(spec, pageable);

        assertThat(findReviewList).isNotNull();
    }
}
