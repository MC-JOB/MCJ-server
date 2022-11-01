package com.minecraft.job.common.teamlogo.service;

import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.teamlogo.domain.TeamLogo;
import com.minecraft.job.common.teamlogo.domain.TeamLogoRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static com.minecraft.job.common.teamlogo.domain.TeamLogoStatus.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;


@Transactional
@SpringBootTest
class DomainTeamLogoServiceTest {

    @Autowired
    private TeamLogoService teamLogoService;

    @Autowired
    private TeamLogoRepository teamLogoRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    private Team team;
    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());
        team = teamRepository.save(TeamFixture.create(user));
    }

    @Test
    void 팀로고_생성_성공() {
        TeamLogo teamLogo = teamLogoService.create(team.getId(), user.getId(), "name", "savedName", 1L);

        TeamLogo findTeamLogo = teamLogoRepository.findById(teamLogo.getId()).orElseThrow();

        assertThat(findTeamLogo.getId()).isNotNull();
        assertThat(findTeamLogo.getName()).isEqualTo("name");
        assertThat(findTeamLogo.getSavedName()).isEqualTo("savedName");
        assertThat(findTeamLogo.getSize()).isEqualTo(1L);
        assertThat(findTeamLogo.getTeam()).isSameAs(team);
        assertThat(findTeamLogo.getStatus()).isEqualTo(CREATED);
        assertThat(findTeamLogo.getCreatedAt()).isNotNull();
    }

    @Test
    void 팀로고_생성_실패_유저의_팀이_아님() {
        User fakerUser = userRepository.save(UserFixture.getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(
                () -> teamLogoService.create(team.getId(), fakerUser.getId(), "name", "savedName", 1L)
        );
    }

    @Test
    void 팀로고_수정_성공() {
        TeamLogo teamLogo = teamLogoService.create(team.getId(), user.getId(), "name", "savedName", 1L);

        teamLogoService.update(teamLogo.getId(), team.getId(), user.getId(), "updateName", "updateSavedName", 3L);

        TeamLogo findTeamLogo = teamLogoRepository.findById(teamLogo.getId()).orElseThrow();

        assertThat(findTeamLogo.getId()).isNotNull();
        assertThat(findTeamLogo.getName()).isEqualTo("updateName");
        assertThat(findTeamLogo.getSavedName()).isEqualTo("updateSavedName");
        assertThat(findTeamLogo.getSize()).isEqualTo(3L);
    }

    @Test
    void 팀로고_수정_실패__팀로고의_팀이_아님() {
        TeamLogo teamLogo = teamLogoService.create(team.getId(), user.getId(), "name", "savedName", 1L);

        Team fakeTeam = teamRepository.save(TeamFixture.getFakeTeam(user));

        assertThatIllegalArgumentException().isThrownBy(
                () -> teamLogoService.update(teamLogo.getId(), fakeTeam.getId(), user.getId(), "updateName", "updateSavedName", 3L)
        );
    }

    @Test
    void 팀로고_수정_실패__유저의_팀이_아님() {
        TeamLogo teamLogo = teamLogoService.create(team.getId(), user.getId(), "name", "savedName", 1L);

        User fakerUser = userRepository.save(UserFixture.getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(
                () -> teamLogoService.update(teamLogo.getId(), team.getId(), fakerUser.getId(), "updateName", "updateSavedName", 3L)
        );
    }
}
