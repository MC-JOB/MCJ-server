package com.minecraft.job.common.emailauth.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class EmailAuthTest {

    @Test
    void 이메일_인증_생성() {
        EmailAuth emailAuth = EmailAuth.create("email");

        assertThat(emailAuth.getEmail()).isEqualTo("email");
    }

    @ParameterizedTest
    @NullAndEmptySource()
    void 이메일_인증_생성_실패(String email) {
        assertThatIllegalArgumentException().isThrownBy(() -> EmailAuth.create(email));
    }
}
