package com.minecraft.job.common.emailauth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static com.minecraft.job.common.support.Preconditions.require;
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

    private OffsetDateTime sentAt;

    private EmailAuth(String email) {
        this.email = email;
    }

    public static EmailAuth create(String email) {
        require(Strings.isNotBlank(email));

        return new EmailAuth(email);
    }
}
