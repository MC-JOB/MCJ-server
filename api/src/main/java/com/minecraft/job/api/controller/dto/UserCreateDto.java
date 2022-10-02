package com.minecraft.job.api.controller.dto;

import com.minecraft.job.common.user.domain.User;

public class UserCreateDto {

    public record UserCreateRequest(
            String email, String password,
            String nickname, String interest, Long age
    ) {
    }

    public record UserCreateResponse(
            UserCreateData user
    ) {
        public static UserCreateResponse create(UserCreateData userCreateData) {
            return new UserCreateResponse(userCreateData);
        }
    }

    public record UserCreateData(Long id, String nickname) {

        public static UserCreateData create(User user) {
            return new UserCreateData(user.getId(), user.getNickname());
        }
    }
}
