package com.minecraft.job.common.emailauth.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.minecraft.job.common.emailauth.domain.EmailAuth.MAX_CODE_SIZE;
import static com.minecraft.job.common.emailauth.domain.EmailAuth.MAX_TRY_COUNT;
import static com.minecraft.job.common.emailauth.domain.EmailAuthStatus.CREATED;
import static com.minecraft.job.common.emailauth.domain.EmailAuthStatus.ISSUED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class EmailAuthTest {

    @Test
    void 이메일_인증_생성() {
        EmailAuth emailAuth = EmailAuth.create("email");

        assertThat(emailAuth.getEmail()).isEqualTo("email");
        assertThat(emailAuth.getStatus()).isEqualTo(CREATED);
    }

    @ParameterizedTest
    @NullAndEmptySource()
    void 이메일_인증_생성_실패(String email) {
        assertThatIllegalArgumentException().isThrownBy(() -> EmailAuth.create(email));
    }

    @Test
    void 이메일_인증_발급() {
        EmailAuth emailAuth = EmailAuth.create("email");

        emailAuth.issue();

        assertThat(emailAuth.getCode()).hasSize(MAX_CODE_SIZE);
        assertThat(emailAuth.getTryCount()).isEqualTo(MAX_TRY_COUNT);
        assertThat(emailAuth.getSentAt()).isNotNull();
        assertThat(emailAuth.getStatus()).isEqualTo(ISSUED);
    }
}
