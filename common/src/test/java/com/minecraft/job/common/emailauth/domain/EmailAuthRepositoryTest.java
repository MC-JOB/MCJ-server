package com.minecraft.job.common.emailauth.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class EmailAuthRepositoryTest {

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @Test
    void 이메일_인증_생성_성공() {
        EmailAuth emailAuth = EmailAuth.create("email");

        emailAuthRepository.save(emailAuth);

        emailAuthRepository.findById(emailAuth.getId());

        Assertions.assertThat(emailAuth.getId()).isNotNull();
        Assertions.assertThat(emailAuth.getEmail()).isEqualTo("email");
    }
}
