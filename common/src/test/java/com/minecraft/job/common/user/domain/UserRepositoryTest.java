package com.minecraft.job.common.user.domain;

import com.minecraft.job.common.user.domain.Specification.UserSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import static com.minecraft.job.common.user.domain.UserStatus.ACTIVATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private void createUsers() {
        for (int i = 1; i <= 20; i++) {
            User user = User.create("email" + i, "password" + i, "nickname", "interest", 10L);
            userRepository.save(user);
        }
    }

    @Test
    void 유저_생성_성공() {
        User user = User.create("email", "password", "nickname", "interest", 10L);

        user = userRepository.save(user);

        User findUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(findUser.getEmail()).isEqualTo("email");
        assertThat(findUser.getPassword()).isEqualTo("password");
        assertThat(findUser.getNickname()).isEqualTo("nickname");
        assertThat(findUser.getInterest()).isEqualTo("interest");
        assertThat(findUser.getAge()).isEqualTo(10L);
        assertThat(findUser.getStatus()).isEqualTo(ACTIVATED);
        assertThat(findUser.getCreatedAt()).isNotNull();
    }

    @Test
    void 유저_리스트_조회_성공__이메일이_일치하는_경우() {
        createUsers();

        Specification<User> spec = Specification.where(UserSpecification.equalEmail("email1"));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<User> findUserList = userRepository.findAll(spec, pageRequest);

        assertThat(findUserList.getNumberOfElements()).isEqualTo(1);
        assertThat(findUserList.getContent().get(0).getEmail()).isEqualTo("email1");
    }

    @Test
    void 유저_리스트_조회_성공__닉네임이_일치하는_경우() {
        createUsers();

        Specification<User> spec = Specification.where(UserSpecification.equalNickname("nickname"));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<User> findUserList = userRepository.findAll(spec, pageRequest);

        assertThat(findUserList.getNumberOfElements()).isEqualTo(10);
        assertThat(findUserList.getContent().get(0).getNickname()).isEqualTo("nickname");
    }

    @Test
    void 유저_리스트_조회_성공__관심사가_포함되는_경우() {
        createUsers();

        Specification<User> spec = Specification.where(UserSpecification.likeInterest("interest"));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<User> findUserList = userRepository.findAll(spec, pageRequest);

        assertThat(findUserList.getNumberOfElements()).isEqualTo(10);
        assertThat(findUserList.getContent().get(0).getInterest()).isEqualTo("interest");
    }

    @Test
    void 유저_리스트_조회_성공__페이징_처리() {
        createUsers();

        Specification<User> spec = Specification.where(UserSpecification.likeInterest("interest"));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<User> findUserList = userRepository.findAll(spec, pageRequest);

        assertThat(findUserList.getTotalPages()).isEqualTo(2);
    }
}
