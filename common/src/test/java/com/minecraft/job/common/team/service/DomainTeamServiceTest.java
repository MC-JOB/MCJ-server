package com.minecraft.job.common.team.service;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.minecraft.job.common.fixture.UserFixture.create;
import static com.minecraft.job.common.fixture.UserFixture.getFakerUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
@Transactional
class DomainTeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(create());
    }

    @Test
    void 팀_생성_성공() {
        Team team = teamService.create(user.getId(), "name", "description", "logo", 5L);

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getId()).isNotNull();
    }

    @Test
    void 팀_수정_성공() {
        Team team = teamService.create(user.getId(), "name", "description", "logo", 5L);

        teamService.update(team.getId(), user.getId(), "updateName", "updateDescription", "updateLogo", 10L);

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getName()).isEqualTo("updateName");
        assertThat(findTeam.getDescription()).isEqualTo("updateDescription");
        assertThat(findTeam.getLogo()).isEqualTo("updateLogo");
        assertThat(findTeam.getMemberNum()).isEqualTo(10L);
    }

    @Test
    void 팀_수정_실패_유저의_팀이_아님() {
        Team team = teamService.create(user.getId(), "name", "description", "logo", 5L);

        User fakerUser = userRepository.save(getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(
                () -> teamService.update(team.getId(), fakerUser.getId(), "updateName", "updateDescription", "updateLogo", 10L)
        );
    }
}
