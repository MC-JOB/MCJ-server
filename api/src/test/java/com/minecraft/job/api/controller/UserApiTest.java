package com.minecraft.job.api.controller;

import com.minecraft.job.api.support.ApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.minecraft.job.api.controller.dto.UserCreateDto.UserCreateRequest;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserApiTest extends ApiTest {

    @Test
    void 유저_생성() throws Exception {
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
}