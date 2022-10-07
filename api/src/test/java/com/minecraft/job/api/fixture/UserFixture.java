package com.minecraft.job.api.fixture;

import com.minecraft.job.common.user.domain.User;

public class UserFixture {

    public static User create() {
        return User.create("email", "password", "nickname", "interest", 10L);
    }

    public static User getFakerUser() {
        return User.create("fakerEmail", "fakerPassword", "fakerNickname", "fakerInterest", 10L);
    }
}
