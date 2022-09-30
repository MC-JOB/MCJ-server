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

    private String  nickname;

    private String email;

    private String password;

    private Long age;

    private String interests;

    @Enumerated(value = EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVATED;

    private LocalDateTime createdAt = LocalDateTime.now();

    private User(String nickname, String email, String password, Long age ){
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public static User create(String nickname, String email, String password, Long age ){
        require(Strings.isNotBlank(nickname));
        require(Strings.isNotBlank(email));
        require(Strings.isNotBlank(password));
        require(age >= 0);
        return new User(nickname,email,password,age);
    }
}
