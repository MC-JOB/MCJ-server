package com.minecraft.job.common.recruitmentProcess.domain;

import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.minecraft.job.common.recruitmentProcess.domain.RecruitmentProcessStatus.*;
import static com.minecraft.job.common.support.Preconditions.*;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * 시나리오
 * 처음 생성
 * 상태 = WAITING
 * <p>
 * 서류 합격
 * 상태 = IN_PROGRESS
 * <p>
 * 진행중 상태에서 최종합격
 * 상태 = PASSED
 * <p>
 * 중도 취소
 * 상태 = CANCELED
 * <p>
 * 불합격
 * 상태 = FAILED
 *
 * <p>
 * closedAt 활성화
 * 상태 - PASSED, CANCELED, FAILED
 */

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uq_recruitment_user",
                columnNames = {"recruitment_id", "user_id"}
        )
})
public class RecruitmentProcess {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(value = EnumType.STRING)
    private RecruitmentProcessStatus status = WAITING;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime closedAt;

    private RecruitmentProcess(Recruitment recruitment, User user) {
        this.recruitment = recruitment;
        this.user = user;
    }

    public static RecruitmentProcess create(Recruitment recruitment, User user) {
        notNull(recruitment);
        notNull(user);

        require(!recruitment.ofUser(user));

        return new RecruitmentProcess(recruitment, user);
    }

    public void inProgress() {
        check(this.status == WAITING);

        this.status = IN_PROGRESS;
    }

    public void canceled() {
        check(CAN_MOVE_CANCELED.contains(status));

        this.status = CANCELED;
        this.closedAt = LocalDateTime.now();
    }

    public void passed() {
        check(this.status == IN_PROGRESS);

        this.status = PASSED;
        this.closedAt = LocalDateTime.now();
    }
}
