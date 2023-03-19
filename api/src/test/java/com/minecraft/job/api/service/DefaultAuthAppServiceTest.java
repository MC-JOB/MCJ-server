package com.minecraft.job.api.service;

import com.minecraft.job.api.component.JwtComponent;
import com.minecraft.job.api.fixture.EmailAuthFixture;
import com.minecraft.job.api.fixture.UserFixture;
import com.minecraft.job.common.emailauth.domain.EmailAuthRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static com.minecraft.job.api.component.JwtType.ACCESS;
import static com.minecraft.job.api.component.JwtType.REFRESH;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Transactional
class DefaultAuthAppServiceTest {

    private static final String userPassword = "1234";
    @Autowired
    private AuthAppService authAppService;
    @Autowired
    private EmailAuthRepository emailAuthRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtComponent jwtComponent;
    private User user;

    @BeforeEach
    void setUp() {
        emailAuthRepository.save(EmailAuthFixture.getValidatedEmailAuth("email"));

        String password = passwordEncoder.encode(userPassword);
        user = userRepository.save(UserFixture.create("email", password));
    }

    @Test
    void 로그인_성공() {
        assertDoesNotThrow(() -> {
                    Pair<String, String> loginTokens = authAppService.login(user.getEmail(), userPassword);

                    jwtComponent.validate(loginTokens.getFirst(), ACCESS);
                    jwtComponent.validate(loginTokens.getFirst(), REFRESH);
                }
        );
    }
}
