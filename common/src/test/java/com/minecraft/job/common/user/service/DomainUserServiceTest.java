package com.minecraft.job.common.user.service;

import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.support.MinecraftJobException;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
class DomainUserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.getAntherUser("setUp"));
    }

    @Test
    void 유저_생성_성공() {
        User user = userService.create("email", "password", "nickname", "interest", 10L);

        User findUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(findUser.getId()).isEqualTo(user.getId());
    }

    @Test
    void 유저_생성_실패__이미_존재하는_이메일() {
        userService.create("email", "password", "nickname", "interest", 10L);

        assertThatExceptionOfType(MinecraftJobException.class).isThrownBy(
                () -> userService.create("email", "password", "nickname", "interest", 10L)
        );
    }

    @Test
    void 유저_정보_변경() {
        userService.changeInformation(user.getId(), "changeNickname", "changeInterest", 30L);

        assertThat(user.getNickname()).isEqualTo("changeNickname");
        assertThat(user.getInterest()).isEqualTo("changeInterest");
        assertThat(user.getAge()).isEqualTo(30L);
    }
}
