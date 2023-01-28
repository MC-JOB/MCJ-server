package com.minecraft.job.common.team.service;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.team.domain.TeamSearchType;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import static com.minecraft.job.common.fixture.UserFixture.create;
import static com.minecraft.job.common.fixture.UserFixture.getFakerUser;
import static com.minecraft.job.common.team.domain.TeamStatus.ACTIVATED;
import static com.minecraft.job.common.team.domain.TeamStatus.INACTIVATED;
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
        Team team = teamService.create(user.getId(), "name", "description", 5L);

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getId()).isNotNull();
    }

    @Test
    void 팀_평점_적용_성공() {
        Team team = teamService.create(user.getId(), "name", "description", 5L);

        teamService.applyAveragePoint(team.getId(), 3.0);

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getAveragePoint()).isEqualTo(3L);
    }

    @Test
    void 팀_수정_성공() {
        Team team = teamService.create(user.getId(), "name", "description", 5L);

        teamService.update(team.getId(), user.getId(), "updateName", "updateDescription", 10L);

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getName()).isEqualTo("updateName");
        assertThat(findTeam.getDescription()).isEqualTo("updateDescription");
        assertThat(findTeam.getMemberNum()).isEqualTo(10L);
    }

    @Test
    void 팀_수정_실패_유저의_팀이_아님() {
        Team team = teamService.create(user.getId(), "name", "description", 5L);

        User fakerUser = userRepository.save(getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(
                () -> teamService.update(team.getId(), fakerUser.getId(), "updateName", "updateDescription", 10L)
        );
    }

    @Test
    void 팀_비활성화_성공() {
        Team team = teamService.create(user.getId(), "name", "description", 5L);

        teamService.inactivate(team.getId(), user.getId());

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getStatus()).isEqualTo(INACTIVATED);
    }

    @Test
    void 팀_비활성화_실패_유저의_팀이_아님() {
        Team team = teamService.create(user.getId(), "name", "description", 5L);

        User fakerUser = userRepository.save(getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(
                () -> teamService.inactivate(team.getId(), fakerUser.getId())
        );
    }


    @Test
    void 팀_활성화_성공() {
        Team team = teamService.create(user.getId(), "name", "description", 5L);

        team.inactivate();

        teamService.activate(team.getId(), user.getId());

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getStatus()).isEqualTo(ACTIVATED);
    }

    @Test
    void 팀_활성화_실패_유저의_팀이_아님() {
        Team team = teamService.create(user.getId(), "name", "description", 5L);

        team.inactivate();

        User fakerUser = userRepository.save(getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(
                () -> teamService.activate(team.getId(), fakerUser.getId())
        );
    }

    @Test
    void 팀_리스트_조회_성공__이름이_포함되는_경우() {
        String name = "name";
        팀_목록_생성(name, "description", 5L, user);

        Page<Team> findTeamList = teamService.getTeams(TeamSearchType.NAME, name, 0);

        for (Team team : findTeamList) {
            assertThat(team.getName()).contains(name);
        }
    }

    @Test
    void 팀_리스트_조회_성공__설명이_포함되는_경우() {
        String description = "description";
        팀_목록_생성("name", description, 5L, user);

        Page<Team> findTeamList = teamService.getTeams(TeamSearchType.DESCRIPTION, description, 0);

        for (Team team : findTeamList) {
            assertThat(team.getDescription()).contains(description);
        }
    }

    @Test
    void 팀_리스트_조회_성공__유저가_일치하는_경우() {
        팀_목록_생성("name", "description", 5L, user);

        Page<Team> findTeamList = teamService.getTeams(TeamSearchType.USER, user.getNickname(), 0);

        for (Team team : findTeamList) {
            assertThat(team.getUser()).isEqualTo(user);
        }
    }

    @Test
    void 팀_리스트_조회_성공__페이징_처리() {
        팀_목록_생성("name", "description", 5L, user);

        Page<Team> findTeamtList = teamService.getTeams(TeamSearchType.NAME, "name", 0);

        assertThat(findTeamtList.getTotalPages()).isEqualTo(2);
    }


    private void 팀_목록_생성(String name, String description, Long memberNum, User user) {
        for (int i = 1; i <= 20; i++) {
            Team team = Team.create(name + 1, description + i, memberNum, user);
            teamRepository.save(team);
        }
    }
}
