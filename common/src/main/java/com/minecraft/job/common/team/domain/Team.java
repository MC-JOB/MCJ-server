package com.minecraft.job.common.team.domain;


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

    private String logo;

    private Long memberNum;

    @ManyToOne(fetch = LAZY)
    private User user;

    @Enumerated(value = EnumType.STRING)
    private TeamStatus status = ACTIVATED;

    private final Long averagePoint = 0L;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private Team(String name, String description, String logo, Long memberNum, User user) {
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.memberNum = memberNum;
        this.user = user;
    }

    public static Team create(String name, String description, String logo, Long memberNum, User user) {
        notNull(user);

        require(Strings.isNotBlank(name));
        require(memberNum >= 0);


        return new Team(name, description, logo, memberNum, user);
    }

    public void update(String name, String description, String logo, Long memberNum) {
        require(Strings.isNotBlank(name));
        require(memberNum >= 0);

        check(this.status == ACTIVATED);

        this.name = name;
        this.description = description;
        this.logo = logo;
        this.memberNum = memberNum;
    }

    public void inactivate() {
        check(this.status == ACTIVATED);

        this.status = INACTIVATED;
    }
}
