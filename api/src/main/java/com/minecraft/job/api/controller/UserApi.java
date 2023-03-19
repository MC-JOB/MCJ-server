package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.UserChangeInformationDto.UserChangeInformationRequest;
import com.minecraft.job.api.controller.dto.UserChangePasswordDto.UserChangePasswordRequest;
import com.minecraft.job.api.controller.dto.UserCreateDto.UserCreateData;
import com.minecraft.job.api.controller.dto.UserCreateDto.UserCreateRequest;
import com.minecraft.job.api.controller.dto.UserCreateDto.UserCreateResponse;
import com.minecraft.job.api.security.user.DefaultMcjUser;
import com.minecraft.job.api.service.UserAppService;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.minecraft.job.api.controller.dto.UserGetInformationDto.UserGetInformationData;
import static com.minecraft.job.api.controller.dto.UserGetInformationDto.UserGetInformationResponse;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;
    private final UserAppService userAppService;

    @PostMapping
    public UserCreateResponse create(
            @RequestBody UserCreateRequest req
    ) {
        User user = userAppService.create(req.email(), req.password(), req.nickname(), req.interest(), req.age());

        return UserCreateResponse.create(UserCreateData.create(user));
    }

    @PostMapping("/change-information")
    public void changeInformation(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody UserChangeInformationRequest req
    ) {
        userService.changeInformation(user.getId(), req.nickname(), req.interest(), req.age());
    }

    @PostMapping("/change-password")
    public void changePassword(
            @AuthenticationPrincipal DefaultMcjUser user,
            @RequestBody UserChangePasswordRequest req
    ) {
        userService.changePassword(user.getId(), req.password(), req.newPassword());
    }

    @PostMapping("/activate")
    public void activate(
            @AuthenticationPrincipal DefaultMcjUser user
    ) {
        userService.activate(user.getId());
    }

    @PostMapping("/inactivate")
    public void inactivate(
            @AuthenticationPrincipal DefaultMcjUser user
    ) {
        userService.inactivate(user.getId());
    }

    @GetMapping("/get-information")
    public UserGetInformationResponse getInformation(
            @AuthenticationPrincipal DefaultMcjUser user
    ) {
        User userInfo = userService.getInformation(user.getId());

        return UserGetInformationResponse.getInformation(UserGetInformationData.getInformationData(userInfo));
    }
}
