package com.minecraft.job.common.emailauth.domain;

import com.minecraft.job.common.support.MinecraftJobException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.OffsetDateTime;

import static com.minecraft.job.common.emailauth.domain.EmailAuth.*;
import static com.minecraft.job.common.emailauth.domain.EmailAuthStatus.*;
import static org.assertj.core.api.Assertions.*;

class EmailAuthTest {

    private EmailAuth emailAuth;

    @BeforeEach
    void setUp() {
        emailAuth = EmailAuth.create("email");
    }

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
        emailAuth.issue();

        assertThat(emailAuth.getCode()).hasSize(MAX_CODE_SIZE);
        assertThat(emailAuth.getTryCount()).isEqualTo(MAX_TRY_COUNT);
        assertThat(emailAuth.getSentAt()).isNotNull();
        assertThat(emailAuth.getStatus()).isEqualTo(ISSUED);
    }

    @Test
    void 이메일_인증_검증() {
        emailAuth.issue();

        boolean result = emailAuth.validate(emailAuth.getCode());

        assertThat(result).isTrue();
        assertThat(emailAuth.getStatus()).isEqualTo(VALIDATED);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이메일_인증_검증_실패__코드가_널이거나_공백(String code) {
        emailAuth.issue();

        assertThatIllegalArgumentException().isThrownBy(() -> emailAuth.validate(code));
    }

    @Test
    void 이메일_인증_검증_실패__발급됨_상태가_아님() {
        emailAuth.setCode("code");

        assertThatIllegalStateException().isThrownBy(() -> emailAuth.validate(emailAuth.getCode()));
    }

    @Test
    void 이메일_인증_검증_실패__인증_시도_시간_초과() {
        emailAuth.issue();

        emailAuth.setSentAt(OffsetDateTime.now().minusMinutes(MAX_CODE_TRY_TIME));

        assertThatExceptionOfType(MinecraftJobException.class).isThrownBy(() -> emailAuth.validate(emailAuth.getCode()));
    }

    @Test
    void 이메일_인증_검증_실패__인증_시도_횟수_초과() {
        emailAuth.issue();

        emailAuth.setTryCount(0);

        assertThatExceptionOfType(MinecraftJobException.class).isThrownBy(() -> emailAuth.validate(emailAuth.getCode()));
    }

    @Test
    void 이메일_인증_검증_실패__검증_코드가_다름() {
        emailAuth.issue();

        assertThatExceptionOfType(MinecraftJobException.class).isThrownBy(() -> emailAuth.validate("fakerCode"));
    }

    @Test
    void 이메일_인증_검증_확인() {
        emailAuth.issue();

        emailAuth.validate(emailAuth.getCode());

        boolean result = emailAuth.isValidated();

        assertThat(result).isTrue();
    }
}
