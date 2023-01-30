package com.minecraft.job.common.emailauth.service;

import com.minecraft.job.common.emailauth.domain.EmailAuth;
import com.minecraft.job.common.emailauth.domain.EmailAuthRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static com.minecraft.job.common.emailauth.domain.EmailAuthStatus.ISSUED;
import static com.minecraft.job.common.emailauth.domain.EmailAuthStatus.VALIDATED;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DomainEmailServiceTest {

    @Autowired
    private EmailAuthService emailAuthService;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @Test
    void 이메일_인증_발급() {
        emailAuthService.issue("email");

        EmailAuth findEmailAuth = emailAuthRepository.findByEmail("email").orElseThrow();

        assertThat(findEmailAuth.getStatus()).isEqualTo(ISSUED);
        assertThat(findEmailAuth.getCode()).isNotNull();
        assertThat(findEmailAuth.getTryCount()).isNotNull();
        assertThat(findEmailAuth.getSentAt()).isNotNull();
    }

    @Test
    void 이메일_인증_발급__이미_생성됨() {
        emailAuthRepository.save(EmailAuth.create("email"));

        emailAuthService.issue("email");

        EmailAuth findEmailAuth = emailAuthRepository.findByEmail("email").orElseThrow();

        assertThat(findEmailAuth.getStatus()).isEqualTo(ISSUED);
        assertThat(findEmailAuth.getCode()).isNotNull();
        assertThat(findEmailAuth.getTryCount()).isNotNull();
        assertThat(findEmailAuth.getSentAt()).isNotNull();
    }

    @Test
    void 이메일_인증_검증() {
        EmailAuth emailAuth = emailAuthRepository.save(EmailAuth.create("email"));

        emailAuthService.issue("email");

        boolean result = emailAuthService.validate("email", emailAuth.getCode());

        EmailAuth findEmailAuth = emailAuthRepository.findByEmail("email").orElseThrow();

        assertThat(result).isTrue();
        assertThat(findEmailAuth.getStatus()).isEqualTo(VALIDATED);
    }
}
