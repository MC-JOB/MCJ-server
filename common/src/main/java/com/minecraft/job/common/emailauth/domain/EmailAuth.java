package com.minecraft.job.common.emailauth.domain;

import com.minecraft.job.common.support.MinecraftJobException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.utility.RandomString;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static com.minecraft.job.common.emailauth.domain.EmailAuthStatus.*;
import static com.minecraft.job.common.support.ErrorCode.*;
import static com.minecraft.job.common.support.Preconditions.check;
import static com.minecraft.job.common.support.Preconditions.require;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
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

    public static final Long MAX_CODE_TRY_TIME = 5L;

    public static final int MAX_TRY_COUNT = 5;
    public static final int MAX_CODE_SIZE = 5;
    private OffsetDateTime sentAt;

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

    public boolean validate(String code) {
        require(Strings.isNotBlank(code));

        check(this.status == ISSUED);

        tryCountTimeValidate();
        tryCountValidate();
        codeValidate(code);

        this.status = VALIDATED;

        return true;
    }

    public boolean isValidated() {
        return status == VALIDATED;
    }

    private void tryCountTimeValidate() {
        if (OffsetDateTime.now().isAfter(this.sentAt.plusMinutes(MAX_CODE_TRY_TIME))) {
            this.status = FAILED;

            throw new MinecraftJobException(CODE_VALID_TIME_EXCEEDED);
        }
    }

    private void tryCountValidate() {
        if (!(tryCount > 0)) {
            this.status = FAILED;

            throw new MinecraftJobException(TRY_COUNT_EXCEEDED);
        }
    }

    private void codeValidate(String code) {
        if (!this.code.equals(code)) {
            tryCount--;

            throw new MinecraftJobException(CODE_NOT_VALID);
        }
    }
}
