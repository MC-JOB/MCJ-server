package com.minecraft.job.common.user.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.minecraft.job.common.support.Preconditions.require;
import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private String interest;

    private Long age;

    @Enumerated(value = EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVATED;

    private LocalDateTime createdAt = LocalDateTime.now();

    private User(String email, String password, String nickname, String interest, Long age) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.interest = interest;
        this.age = age;
    }

    public static User create(String email, String password, String nickname, String interest, Long age) {
        require(Strings.isNotBlank(email));
        require(Strings.isNotBlank(password));
        require(Strings.isNotBlank(nickname));
        require(age >= 0);

        return new User(email, password, nickname, interest, age);
    }
}
