package user.domain;

import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class UserTest {

    @Test
    void 유저_생성_성공(){
        User user = User.create("nickname","email","password",10L);
        assertThat(user.getNickname()).isEqualTo("nickname");
        assertThat(user.getEmail()).isEqualTo("email");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getAge()).isEqualTo(10L);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 유저_생성_실패__닉네임이_널이거나_공백(String nickname){
        assertThatIllegalArgumentException().isThrownBy(() -> User.create(nickname,"email","password",10L));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 유저_생성_실패__이메일이_널이거나_공백(String email){
        assertThatIllegalArgumentException().isThrownBy(() -> User.create("nickname",email,"password",10L));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 유저_생성_실패__패스워드_널이거나__공백(String password){
        assertThatIllegalArgumentException().isThrownBy(() -> User.create("nickname","email",password,10L));
    }

    @Test
    void 유저_생성_실패__나이_음수(){
        assertThatIllegalArgumentException().isThrownBy(()-> User.create("nickname","email","password",-1L));
    }



}
