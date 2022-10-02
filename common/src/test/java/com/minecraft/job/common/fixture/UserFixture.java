package com.minecraft.job.common.fixture;

import com.minecraft.job.common.user.domain.User;

public class UserFixture {

    public static User create() {
        return User.create("email", "password", "nickname", "interest", 10L);
    }
}