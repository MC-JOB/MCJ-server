package com.minecraft.job.api.service;

import com.minecraft.job.api.fixture.EmailAuthFixture;
import com.minecraft.job.common.emailauth.domain.EmailAuthRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DefaultUserAppServiceTest {

    @Autowired
    private UserAppService userAppService;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void 유저_생성_성공() {
        emailAuthRepository.save(EmailAuthFixture.getValidatedEmailAuth("email"));

        User user = userAppService.create("email", "password", "nickname", "interest", 10L);

        User findUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(passwordEncoder.matches("password", user.getPassword())).isTrue();
    }
}
