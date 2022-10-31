package com.minecraft.job.common.teamlogo.domain;

import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.minecraft.job.common.teamlogo.domain.TeamLogoStatus.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class TeamLogoRepositoryTest {

    @Autowired
    private TeamLogoRepository teamLogoRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    private Team team;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(UserFixture.create());
        team = teamRepository.save(TeamFixture.create(user));
    }

    @Test
    void 팀로고_생성_성공() {
        TeamLogo teamLogo = TeamLogo.create("name", "savedName", 1L, team);

        teamLogo = teamLogoRepository.save(teamLogo);

        TeamLogo findTeamLogo = teamLogoRepository.findById(teamLogo.getId()).orElseThrow();

        assertThat(findTeamLogo.getId()).isNotNull();
        assertThat(findTeamLogo.getName()).isEqualTo("name");
        assertThat(findTeamLogo.getSavedName()).isEqualTo("savedName");
        assertThat(findTeamLogo.getSize()).isEqualTo(1L);
        assertThat(findTeamLogo.getTeam()).isSameAs(team);
        assertThat(findTeamLogo.getStatus()).isEqualTo(CREATED);
        assertThat(findTeamLogo.getCreatedAt()).isNotNull();
    }
}