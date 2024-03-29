package com.minecraft.job.common.user.domain;

import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.support.MinecraftJobException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.minecraft.job.common.user.domain.UserStatus.ACTIVATED;
import static com.minecraft.job.common.user.domain.UserStatus.INACTIVATED;
import static org.assertj.core.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = UserFixture.create();
    }

    @Test
    void 생성_성공() {
        User user = User.create("email", "password", "nickname", "interest", 10L);

        assertThat(user.getEmail()).isEqualTo("email");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getNickname()).isEqualTo("nickname");
        assertThat(user.getInterest()).isEqualTo("interest");
        assertThat(user.getAge()).isEqualTo(10L);
        assertThat(user.getStatus()).isEqualTo(ACTIVATED);
        assertThat(user.getCreatedAt()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 생성_실패__이메일이_널이거나_공백(String email) {
        assertThatIllegalArgumentException().isThrownBy(() -> User.create(email, "password", "nickname", "interest", 10L));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 생성_실패__비밀번호가_널이거나__공백(String password) {
        assertThatIllegalArgumentException().isThrownBy(() -> User.create("email", password, "nickname", "interest", 10L));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 생성_실패__닉네임이_널이거나_공백(String nickname) {
        assertThatIllegalArgumentException().isThrownBy(() -> User.create("email", "password", nickname, "interest", 10L));
    }

    @Test
    void 생성_실패__나이가_음수() {
        assertThatIllegalArgumentException().isThrownBy(() -> User.create("email", "password", "nickname", "interest", -1L));
    }

    @Test
    void 정보_변경() {
        user.changeInformation("changeNickName", "changeInterest", 10L);

        assertThat(user.getNickname()).isEqualTo("changeNickName");
        assertThat(user.getInterest()).isEqualTo("changeInterest");
        assertThat(user.getAge()).isEqualTo(10L);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 정보_변경_실패__닉네임이_널이거나_공백(String nickname) {
        assertThatIllegalArgumentException().isThrownBy(() -> user.changeInformation(nickname, "interest", 10L));
    }

    @Test
    void 정보_변경_실패__나이가_음수() {
        assertThatIllegalArgumentException().isThrownBy(() -> user.changeInformation("nickname", "interest", -1L));
    }

    @Test
    void 정보_변경_실패__비활성화_상태임() {
        user.inactivate();

        assertThatIllegalStateException().isThrownBy(() -> user.changeInformation("changeNickName", "changeInterest", 10L));
    }

    @Test
    void 비밀번호_변경() {
        user.changePassword(user.getPassword(), "newPassword");

        assertThat(user.getPassword()).isEqualTo("newPassword");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 비밀번호_변경_실패__비밀번호가_널이거나__공백(String password) {
        assertThatIllegalArgumentException().isThrownBy(() -> user.changePassword(password, "newPassword"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 비밀번호_변경_실패__새로운_비밀번호가_널이거나__공백(String newPassword) {
        assertThatIllegalArgumentException().isThrownBy(() -> user.changePassword("password", newPassword));
    }

    @Test
    void 비밀번호_변경_실패_비활성화_상태임() {
        user.inactivate();

        assertThatIllegalStateException().isThrownBy(() -> user.changePassword("password", "newPassword"));
    }

    @Test
    void 비밀번호_변경_실패__기존_비밀번호와_다름() {
        user.changePassword("password", "newPassword");

        assertThatExceptionOfType(MinecraftJobException.class).isThrownBy(() -> user.changePassword("fakePassword", "newPassword"));
    }

    @Test
    void 유저_비활성화() {
        user.inactivate();

        assertThat(user.getStatus()).isEqualTo(INACTIVATED);
    }

    @Test
    void 유저_비활성화_실패__활성화_상태가_아님() {
        user.inactivate();

        assertThatIllegalStateException().isThrownBy(() -> user.inactivate());
    }

    @Test
    void 유저_활성화() {
        user.inactivate();

        user.activate();

        assertThat(user.getStatus()).isEqualTo(ACTIVATED);
    }

    @Test
    void 유저_활성화_실패__비활성화_상태가_아님() {
        assertThatIllegalStateException().isThrownBy(() -> user.activate());
    }
}
