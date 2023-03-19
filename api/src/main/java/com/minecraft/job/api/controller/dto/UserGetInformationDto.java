package com.minecraft.job.api.controller.dto;

import com.minecraft.job.common.user.domain.User;

public class UserGetInformationDto {

    public record UserGetInformationResponse(UserGetInformationData informationData) {

        public static UserGetInformationResponse getInformation(UserGetInformationData userGetInformationData) {
            return new UserGetInformationResponse(userGetInformationData);
        }

    }

    public record UserGetInformationData(User user) {

        public static UserGetInformationData getInformationData(User user) {
            return new UserGetInformationData(user);
        }
    }
}
