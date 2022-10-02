package com.minecraft.job.common.team.domain;

import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.minecraft.job.common.team.domain.TeamStatus.ACTIVATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());
    }

    @Test
    void 팀_생성_성공() {
        Team team = Team.create("name", "description", "logo", 5L, user);

        team = teamRepository.save(team);

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getId()).isNotNull();
        assertThat(findTeam.getName()).isEqualTo("name");
        assertThat(findTeam.getDescription()).isEqualTo("description");
        assertThat(findTeam.getLogo()).isEqualTo("logo");
        assertThat(findTeam.getMemberNum()).isEqualTo(5L);
        assertThat(findTeam.getUser()).isEqualTo(user);
        assertThat(findTeam.getStatus()).isEqualTo(ACTIVATED);
        assertThat(findTeam.getAveragePoint()).isEqualTo(0L);
        assertThat(findTeam.getCreatedAt()).isNotNull();
    }
}
