package com.minecraft.job.common.resume.domain;

import com.minecraft.job.common.user.domain.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.minecraft.job.common.resume.domain.ResumeStatue.CREATED;
import static com.minecraft.job.common.support.Preconditions.notNull;
import static com.minecraft.job.common.support.Preconditions.require;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
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
}
