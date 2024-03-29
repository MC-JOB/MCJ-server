package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.user.UserChangePasswordDto.UserChangePasswordRequest;
import com.minecraft.job.api.fixture.EmailAuthFixture;
import com.minecraft.job.api.support.ApiTest;
import com.minecraft.job.common.emailauth.domain.EmailAuthRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.minecraft.job.api.controller.dto.user.UserChangeInformationDto.UserChangeInformationRequest;
import static com.minecraft.job.api.controller.dto.user.UserCreateDto.UserCreateRequest;
import static com.minecraft.job.common.user.domain.UserStatus.ACTIVATED;
import static com.minecraft.job.common.user.domain.UserStatus.INACTIVATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserApiTest extends ApiTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = prepareLoggedInUser("setUp");
        emailAuthRepository.save(EmailAuthFixture.getValidatedEmailAuth(user.getEmail()));
    }

    @Test
    @WithUserDetails
    void 유저_생성() throws Exception {
        emailAuthRepository.save(EmailAuthFixture.getValidatedEmailAuth("email"));

        UserCreateRequest req = new UserCreateRequest("email", "password", "nickname", "interest", 10L);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.user.id").isNotEmpty(),
                        jsonPath("$.user.nickname").value("nickname")
                ).andDo(document("user/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @WithUserDetails
    void 유저_정보_수정() throws Exception {
        UserChangeInformationRequest req = new UserChangeInformationRequest("updateNickname", "updateInterest", 30L);

        mockMvc.perform(post("/user/change-information")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("user/change-information",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        User findUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(findUser.getNickname()).isEqualTo("updateNickname");
        assertThat(findUser.getInterest()).isEqualTo("updateInterest");
        assertThat(findUser.getAge()).isEqualTo(30L);
    }

    @Test
    @WithUserDetails
    void 유저_비밀번호_변경() throws Exception {
        UserChangePasswordRequest req = new UserChangePasswordRequest(user.getPassword(), "newPassword");

        mockMvc.perform(post("/user/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("user/change-password",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        User findUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(findUser.getPassword()).isEqualTo("newPassword");
    }

    @Test
    @WithUserDetails
    void 유저_활성화() throws Exception {
        user.inactivate();

        mockMvc.perform(post("/user/activate"))
                .andExpect(status().isOk())
                .andDo(document("user/activate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        User findUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(findUser.getStatus()).isEqualTo(ACTIVATED);
    }

    @Test
    @WithUserDetails
    void 유저_비활성화() throws Exception {
        mockMvc.perform(post("/user/inactivate"))
                .andExpect(status().isOk())
                .andDo(document("user/inactivate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        User findUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(findUser.getStatus()).isEqualTo(INACTIVATED);
    }

    @Test
    @WithUserDetails
    void 유저_정보_조회_성공() throws Exception {
        mockMvc.perform(get("/user/get-information"))
                .andExpect(status().isOk())
                .andDo(document("user/get-information",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
        User findUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(findUser).isEqualTo(user);
    }
}
