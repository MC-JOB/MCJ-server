package com.minecraft.job.common.team.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

import static com.minecraft.job.common.team.domain.TeamStatus.CREATED;
import static com.minecraft.job.common.util.Preconditions.require;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long Id;

    private String name;

    private String email;

    private String password;

    private String description;

    private String logo;

    private Long memberNum;

    private TeamStatus teamStatus = CREATED;

    private Long averagePoint = 0L;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Team(String name, String email, String password, String description, String logo, Long memberNum) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.description = description;
        this.logo = logo;
        this.memberNum = memberNum;
    }

    public static Team create(String name, String email, String password, String description, String logo, Long memberNum) {
        require(Strings.isNotBlank(name));
        require(Strings.isNotBlank(email));
        require(Strings.isNotBlank(password));
        require(memberNum >= 0);

        return new Team(name, email, password, description, logo, memberNum);
    }
}
