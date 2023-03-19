package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.emailauth.EmailAuthIssueDto.EmailAuthIssueRequest;
import com.minecraft.job.api.fixture.EmailAuthFixture;
import com.minecraft.job.api.support.ApiTest;
import com.minecraft.job.common.emailauth.domain.EmailAuth;
import com.minecraft.job.common.emailauth.domain.EmailAuthRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.minecraft.job.api.controller.dto.emailauth.EmailAuthValidateDto.EmailAuthValidateRequest;
import static com.minecraft.job.common.emailauth.domain.EmailAuthStatus.ISSUED;
import static com.minecraft.job.common.emailauth.domain.EmailAuthStatus.VALIDATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EmailAuthApiTest extends ApiTest {

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @Test
    void 이메일_인증_발급() throws Exception {
        EmailAuthIssueRequest req = new EmailAuthIssueRequest("email");

        mockMvc.perform(post("/email-auth/issue")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("email-auth/issue",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        EmailAuth findEmailAuth = emailAuthRepository.findByEmail("email").orElseThrow();

        assertThat(findEmailAuth.getStatus()).isEqualTo(ISSUED);
    }

    @Test
    void 이메일_인증_검증() throws Exception {
        EmailAuth emailAuth = emailAuthRepository.save(EmailAuthFixture.getIssuedEmailAuth("email"));

        EmailAuthValidateRequest req = new EmailAuthValidateRequest(emailAuth.getEmail(), emailAuth.getCode());

        mockMvc.perform(post("/email-auth/validate")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.isValidate").value(true))
                .andDo(document("email-auth/validate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        EmailAuth findEmailAuth = emailAuthRepository.findByEmail("email").orElseThrow();

        assertThat(findEmailAuth.getStatus()).isEqualTo(VALIDATED);
    }
}
