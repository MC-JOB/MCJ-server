package com.minecraft.job.common.team.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.minecraft.job.common.team.domain.TeamStatus.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void 팀_생성_성공() {
        Team team = Team.create("name", "email", "password", "description", "logo", 5L);

        team = teamRepository.save(team);

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getId()).isNotNull();
        assertThat(findTeam.getName()).isEqualTo("name");
        assertThat(findTeam.getEmail()).isEqualTo("email");
        assertThat(findTeam.getPassword()).isEqualTo("password");
        assertThat(findTeam.getDescription()).isEqualTo("description");
        assertThat(findTeam.getLogo()).isEqualTo("logo");
        assertThat(findTeam.getMemberNum()).isEqualTo(5L);
        assertThat(findTeam.getTeamStatus()).isEqualTo(CREATED);
        assertThat(findTeam.getAveragePoint()).isEqualTo(0L);
        assertThat(findTeam.getCreatedAt()).isNotNull();
    }
}
