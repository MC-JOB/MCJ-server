package com.minecraft.job.common.recruitment.domain;

import com.minecraft.job.common.team.domain.Team;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.minecraft.job.common.recruitment.domain.RecruitmentStatus.CREATED;
import static com.minecraft.job.common.support.Preconditions.notNull;
import static com.minecraft.job.common.support.Preconditions.require;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Enumerated(value = EnumType.STRING)
    private final RecruitmentStatus status = CREATED;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime closedAt;

    private Recruitment(String title, String content, Team team) {
        this.title = title;
        this.content = content;
        this.team = team;
    }

    public static Recruitment create(String title, String content, Team team) {
        require(Strings.isNotBlank(title));
        require(Strings.isNotBlank(content));

        notNull(team);

        return new Recruitment(title, content, team);
    }
}
