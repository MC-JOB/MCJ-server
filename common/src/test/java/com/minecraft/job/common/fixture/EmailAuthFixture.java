package com.minecraft.job.common.fixture;

import com.minecraft.job.common.emailauth.domain.EmailAuth;

import static com.minecraft.job.common.emailauth.domain.EmailAuthStatus.VALIDATED;

public class EmailAuthFixture {

    public static EmailAuth getValidatedEmailAuth(String email) {
        EmailAuth emailAuth = EmailAuth.create(email);
        emailAuth.setStatus(VALIDATED);

        return emailAuth;
    }
}
