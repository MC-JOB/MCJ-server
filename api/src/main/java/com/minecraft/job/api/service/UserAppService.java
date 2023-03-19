package com.minecraft.job.api.service;

import com.minecraft.job.common.user.domain.User;

public interface UserAppService {
    User create(
            String email, String password,
            String nickname, String interest, Long age
    );
}
