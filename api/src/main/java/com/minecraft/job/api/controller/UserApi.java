package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.UserChangeInformationDto.UserChangeInformationRequest;
import com.minecraft.job.api.controller.dto.UserChangePasswordDto.UserChangePasswordRequest;
import com.minecraft.job.api.controller.dto.UserCreateDto.UserCreateData;
import com.minecraft.job.api.controller.dto.UserCreateDto.UserCreateRequest;
import com.minecraft.job.api.controller.dto.UserCreateDto.UserCreateResponse;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;

    @PostMapping
    public UserCreateResponse create(
            @RequestBody UserCreateRequest req
    ) {
        User user = userService.create(req.email(), req.password(), req.nickname(), req.interest(), req.age());

        return UserCreateResponse.create(UserCreateData.create(user));
    }

    @PostMapping("/change-information")
    public void changeInformation(
            @RequestBody UserChangeInformationRequest req
    ) {
        userService.changeInformation(req.userId(), req.nickname(), req.interest(), req.age());
    }

    @PostMapping("/change-password")
    public void changePassword(
            @RequestBody UserChangePasswordRequest req
    ) {
        userService.changePassword(req.userId(), req.password(), req.newPassword());
    }
}
