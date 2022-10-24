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
import static com.minecraft.job.common.review.domain.ReviewStatus.INACTIVATED;
import static com.minecraft.job.common.support.Preconditions.*;

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

    public boolean ofUser(User user) {
        return this.user == user;
    }

    public boolean ofTeam(Team team) {
        return this.team == team;
    }

    public String getTeamName() {
        return team.getName();
    }

    public String getTeamOfLeaderEmail() {
        return team.getLeaderEmail();
    }

    public Double getTeamOfAveragePoint() {
        return team.getAveragePoint();
    }

    public String getUserNickname() {
        return user.getNickname();
    }

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

    public void update(String content, Long score) {
        require(Strings.isNotBlank(content));
        require(score >= MIN_SCORE);
        require(score <= MAX_SCORE);

        check(this.status == ACTIVATED);

        this.content = content;
        this.score = score;
    }

    public void activate() {
        check(this.status != ACTIVATED);

        this.status = ACTIVATED;
    }

    public void inactivate() {
        check(this.status != INACTIVATED);

        this.status = INACTIVATED;
    }
}
