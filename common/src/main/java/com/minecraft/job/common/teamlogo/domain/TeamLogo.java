package com.minecraft.job.common.teamlogo.domain;

import com.minecraft.job.common.team.domain.Team;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.minecraft.job.common.support.Preconditions.notNull;
import static com.minecraft.job.common.support.Preconditions.require;
import static com.minecraft.job.common.teamlogo.domain.TeamLogoStatus.CREATED;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamLogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String savedName;

    private Long size;

    @ManyToOne
    private Team team;

    private final LocalDateTime createdAt = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private TeamLogoStatus status = CREATED;

    private TeamLogo(String name, String savedName, Long size, Team team) {
        this.name = name;
        this.savedName = savedName;
        this.size = size;
        this.team = team;
    }

    public static TeamLogo create(String name, String savedName, Long size, Team team) {
        require(Strings.isNotBlank(name));
        require(Strings.isNotBlank(savedName));
        require(0 < size);

        notNull(team);

        return new TeamLogo(name, savedName, size, team);
    }

    public void update(String name, String savedName, Long size) {
        require(Strings.isNotBlank(name));
        require(Strings.isNotBlank(savedName));
        require(0 < size);

        this.name = name;
        this.savedName = savedName;
        this.size = size;
    }
}
