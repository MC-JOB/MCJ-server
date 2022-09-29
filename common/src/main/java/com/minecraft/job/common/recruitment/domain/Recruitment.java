package com.minecraft.job.common.recruitment.domain;

import com.minecraft.job.common.team.domain.Team;
import lombok.*;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.minecraft.job.common.recruitment.domain.RecruitmentStatus.*;
import static com.minecraft.job.common.support.Preconditions.*;
import static com.minecraft.job.common.support.Preconditions.require;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter
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
    private RecruitmentStatus status = CREATED;

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

    public void activate(LocalDateTime closedAt){
        require(closedAt.isAfter(LocalDateTime.now()));

        check(CAN_MOVE_ACTIVATED.contains(status));

        this.status = ACTIVATED;
        this.closedAt = closedAt;
    }

    public void inactivate(){
        check(CAN_MOVE_INACTIVATED.contains(status));

        this.status = INACTIVATED;
        this.closedAt = null;
    }

    public void createdAtExtend(LocalDateTime closedAt){
        require(closedAt.isAfter(LocalDateTime.now()));
        require(closedAt.isAfter(this.closedAt));

        check(this.status == ACTIVATED);

        this.closedAt = closedAt;
    }

    public void update(String title, String content){
        require(Strings.isNotBlank(title));
        require(Strings.isNotBlank(content));

        check(this.status != DELETED);

        this.title = title;
        this.content = content;
    }

    public void delete(){
        check(this.status != DELETED);

        this.status = DELETED;
    }
}
