package com.minecraft.job.common.resume.domain;

import com.minecraft.job.common.user.domain.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.minecraft.job.common.resume.domain.ResumeStatue.*;
import static com.minecraft.job.common.support.Preconditions.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class Resume {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String trainingHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private ResumeStatue status = CREATED;

    private LocalDateTime createdAt = LocalDateTime.now();

    private Resume(String title, String content, String trainingHistory, User user) {
        this.title = title;
        this.content = content;
        this.trainingHistory = trainingHistory;
        this.user = user;
    }

    public static Resume create(String title, String content, String trainingHistory, User user) {
        notNull(user);

        require(Strings.isNotBlank(title));
        require(Strings.isNotBlank(content));
        require(Strings.isNotBlank(trainingHistory));

        return new Resume(title, content, trainingHistory, user);
    }

    public void update(String title, String content, String trainingHistory) {
        require(Strings.isNotBlank(title));
        require(Strings.isNotBlank(content));
        require(Strings.isNotBlank(trainingHistory));

        check(this.status != DELETED);

        this.title = title;
        this.content = content;
        this.trainingHistory = trainingHistory;
    }

    public void activate() {
        check(CAN_MOVE_ACTIVATED.contains(status));

        this.status = ACTIVATED;
    }

    public void inactivate() {
        check(CAN_MOVE_INACTIVATED.contains(status));

        this.status = INACTIVATED;
    }

    public void delete() {
        check(CAN_MOVE_DELETED.contains(status));

        this.status = DELETED;
    }
}
