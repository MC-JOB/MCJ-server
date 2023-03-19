package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.UserActivateDto.UserActivateRequest;
import com.minecraft.job.api.controller.dto.UserChangeInformationDto.UserChangeInformationRequest;
import com.minecraft.job.api.controller.dto.UserChangePasswordDto.UserChangePasswordRequest;
import com.minecraft.job.api.controller.dto.UserCreateDto.UserCreateData;
import com.minecraft.job.api.controller.dto.UserCreateDto.UserCreateRequest;
import com.minecraft.job.api.controller.dto.UserCreateDto.UserCreateResponse;
import com.minecraft.job.api.controller.dto.UserInactivateDto.UserInactivateRequest;
import com.minecraft.job.api.service.UserAppService;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.minecraft.job.api.controller.dto.UserGetInformationDto.*;

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

    @PostMapping("/activate")
    public void activate(
            @RequestBody UserActivateRequest req
    ) {
        userService.activate(req.userId());
    }

    @PostMapping("/inactivate")
    public void activate(
            @RequestBody UserInactivateRequest req
    ) {
        userService.inactivate(req.userId());
    }

    @GetMapping("/get-information")
    public UserGetInformationResponse getInformation(
            @RequestBody UserGetInformationRequest req
    ) {
        User user = userService.getInformation(req.userId());

        return UserGetInformationResponse.getInformation(UserGetInformationData.getInformationData(user));
    }
}
