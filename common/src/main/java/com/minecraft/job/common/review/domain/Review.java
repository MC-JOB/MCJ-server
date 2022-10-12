package com.minecraft.job.common.review.domain;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.user.domain.User;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.minecraft.job.common.review.domain.ReviewStatus.ACTIVATED;
import static com.minecraft.job.common.support.Preconditions.notNull;
import static com.minecraft.job.common.support.Preconditions.require;

@Getter
@Entity
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uq_user_team", columnNames = {"user_id", "team_id"})
})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Long score;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Enumerated(value = EnumType.STRING)
    private ReviewStatus status = ACTIVATED;

    private final LocalDateTime createdAt = LocalDateTime.now();

    public static final Long MAX_SCORE = 5L;
    public static final Long MIN_SCORE = 0L;

    private Review(String content, Long score, User user, Team team) {
        this.content = content;
        this.score = score;
        this.user = user;
        this.team = team;
    }

    public static Review create(String content, Long score, User user, Team team) {
        notNull(user);
        notNull(team);

        require(!team.ofUser(user));
        require(Strings.isNotBlank(content));
        require(score >= MIN_SCORE);
        require(score <= MAX_SCORE);

        return new Review(content, score, user, team);
    }
}
