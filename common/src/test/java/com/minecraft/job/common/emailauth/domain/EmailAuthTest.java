package com.minecraft.job.common.emailauth.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailAuthTest {

    @Test
    void 이메일_인증_생성() {
        EmailAuth emailAuth = EmailAuth.create("email");

        assertThat(emailAuth.getEmail()).isEqualTo("email");
    }
}
