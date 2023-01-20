package com.minecraft.job.common.emailauth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static com.minecraft.job.common.emailauth.domain.EmailAuthStatus.CREATED;
import static com.minecraft.job.common.support.Preconditions.require;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uq_email_auth_email", columnNames = {"email"})
})
@NoArgsConstructor(access = PROTECTED)
public class EmailAuth {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;

    private String code;

    private int tryCount;

    public static final int MAX_TRY_COUNT = 5;

    private OffsetDateTime sentAt;
    public static final int MAX_CODE_SIZE = 5;
    @Enumerated(value = STRING)
    private EmailAuthStatus status = CREATED;

    private EmailAuth(String email) {
        this.email = email;
    }

    public static EmailAuth create(String email) {
        require(Strings.isNotBlank(email));

        return new EmailAuth(email);
    }

    public void issue() {
        this.code = RandomString.make(MAX_CODE_SIZE);
        this.tryCount = MAX_TRY_COUNT;
        this.sentAt = OffsetDateTime.now();
        this.status = EmailAuthStatus.ISSUED;
    }
}
