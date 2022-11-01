package com.minecraft.job.common.teamlogo.domain;

import com.minecraft.job.common.team.domain.Team;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.minecraft.job.common.support.Preconditions.*;
import static com.minecraft.job.common.teamlogo.domain.TeamLogoStatus.CREATED;
import static com.minecraft.job.common.teamlogo.domain.TeamLogoStatus.DELETED;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uq_team", columnNames = "team_id")
})
public class TeamLogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String savedName;

    private Long size;

    @ManyToOne
    private Team team;

    @Enumerated(EnumType.STRING)
    private TeamLogoStatus status = CREATED;

    private final LocalDateTime createdAt = LocalDateTime.now();

    public boolean ofTeam(Team team) {
        return this.team == team;
    }

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

        check(this.status == CREATED);

        this.name = name;
        this.savedName = savedName;
        this.size = size;
    }

    public void delete() {
        check(this.status != DELETED);

        this.status = DELETED;
    }
}
