package com.minecraft.job.api.fixture;

import com.minecraft.job.common.user.domain.User;

public class UserFixture {

    public static User create() {
        return User.create("email", "password", "nickname", "interest", 10L);
    }

    public static User create(String email, String password) {
        return User.create(email, password, "nickname", "interest", 10L);
    }

    public static User getFakerUser() {
        return User.create("fakerEmail", "fakerPassword", "fakerNickname", "fakerInterest", 10L);
    }

    public static User getAnotherUser(String email) {
        return User.create(email, "fakerPassword", "fakerNickname", "fakerInterest", 10L);
    }
}
