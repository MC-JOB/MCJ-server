package com.minecraft.job.common.team.domain;


import com.minecraft.job.common.review.domain.Review;
import com.minecraft.job.common.user.domain.User;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.minecraft.job.common.support.Preconditions.*;
import static com.minecraft.job.common.team.domain.TeamStatus.ACTIVATED;
import static com.minecraft.job.common.team.domain.TeamStatus.INACTIVATED;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Long memberNum;

    @ManyToOne(fetch = LAZY)
    private User user;

    @Enumerated(value = EnumType.STRING)
    private TeamStatus status = ACTIVATED;

    public static final Double MAX_AVERAGE_POINT = Review.MAX_SCORE.doubleValue();

    private final LocalDateTime createdAt = LocalDateTime.now();
    public static final Double MIN_AVERAGE_POINT = Review.MIN_SCORE.doubleValue();
    private Double averagePoint = 0.0;

    public boolean ofUser(User user) {
        return this.user == user;
    }

    public String getLeaderEmail() {
        return user.getEmail();
    }

    private Team(String name, String description, Long memberNum, User user) {
        this.name = name;
        this.description = description;
        this.memberNum = memberNum;
        this.user = user;
    }

    public static Team create(String name, String description, Long memberNum, User user) {
        notNull(user);

        require(Strings.isNotBlank(name));
        require(memberNum >= 0);


        return new Team(name, description, memberNum, user);
    }

    public void applyAveragePoint(Double averagePoint) {
        require(averagePoint >= MIN_AVERAGE_POINT);
        require(averagePoint <= MAX_AVERAGE_POINT);

        this.averagePoint = averagePoint;
    }

    public void update(String name, String description, Long memberNum) {
        require(Strings.isNotBlank(name));
        require(memberNum >= 0);

        check(this.status == ACTIVATED);

        this.name = name;
        this.description = description;
        this.memberNum = memberNum;
    }

    public void inactivate() {
        check(this.status == ACTIVATED);

        this.status = INACTIVATED;
    }

    public void activate() {
        check(this.status == INACTIVATED);

        this.status = ACTIVATED;
    }
}
