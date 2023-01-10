package com.minecraft.job.common.user.domain;

import lombok.*;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.minecraft.job.common.support.ErrorCode.NOT_SAME_PASSWORD;
import static com.minecraft.job.common.support.Preconditions.*;
import static com.minecraft.job.common.user.domain.UserStatus.ACTIVATED;
import static com.minecraft.job.common.user.domain.UserStatus.INACTIVATED;
import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_email", columnNames = "email")
        }
)
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

    public void changeInformation(String nickname, String interest, Long age) {
        require(Strings.isNotBlank(nickname));
        require(age >= 0);

        check(status == ACTIVATED);

        this.nickname = nickname;
        this.interest = interest;
        this.age = age;
    }

    public void changePassword(String password, String newPassword) {
        require(Strings.isNotBlank(password));
        require(Strings.isNotBlank(newPassword));

        check(status == ACTIVATED);

        validate(this.password.equals(password), NOT_SAME_PASSWORD);

        this.password = newPassword;
    }

    public void inactivate() {
        check(status == ACTIVATED);

        this.status = INACTIVATED;
    }
}
