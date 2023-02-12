package com.minecraft.job.common.team.domain;

import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

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

    private Team team;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());
    }

    @Test
    void 팀_생성_성공() {
        팀_생성("name", "description", 5L, user);

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getId()).isNotNull();
        assertThat(findTeam.getName()).isEqualTo("name");
        assertThat(findTeam.getDescription()).isEqualTo("description");
        assertThat(findTeam.getMemberNum()).isEqualTo(5L);
        assertThat(findTeam.getUser()).isEqualTo(user);
        assertThat(findTeam.getStatus()).isEqualTo(ACTIVATED);
        assertThat(findTeam.getAveragePoint()).isEqualTo(0L);
        assertThat(findTeam.getCreatedAt()).isNotNull();
    }

    @Test
    void 팀_리스트_조회_성공__이름이_포함되는_경우() {
        String name = "name";
        팀_목록_생성(name, "description", 5L, user);

        Specification<Team> spec = Specification.where(TeamSpecification.likeName(name));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Team> findTeamList = teamRepository.findAll(spec, pageRequest);

        assertThat(findTeamList.getNumberOfElements()).isEqualTo(10);
        for (Team team : findTeamList) {
            assertThat(team.getName()).contains(name);
        }
    }

    @Test
    void 팀_리스트_조회_성공__설명이_포함되는_경우() {
        String description = "description";
        팀_목록_생성("name", description, 5L, user);

        Specification<Team> spec = Specification.where(TeamSpecification.likeDescription(description));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Team> findTeamList = teamRepository.findAll(spec, pageRequest);

        assertThat(findTeamList.getNumberOfElements()).isEqualTo(10);
        for (Team team : findTeamList) {
            assertThat(team.getDescription()).contains(description);
        }
    }

    @Test
    void 팀_리스트_조회_성공__유저가_일치하는_경우() {
        팀_목록_생성("name", "description", 5L, user);

        Specification<Team> spec = Specification.where(TeamSpecification.equalUser(user));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Team> findTeamList = teamRepository.findAll(spec, pageRequest);

        assertThat(findTeamList.getNumberOfElements()).isEqualTo(10);
        for (Team team : findTeamList) {
            assertThat(team.getUser()).isEqualTo(user);
        }
    }

    @Test
    void 팀_리스트_조회_성공__페이징_처리() {
        팀_목록_생성("name", "description", 5L, user);

        Specification<Team> spec = Specification.where(TeamSpecification.likeName("name"));

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Team> findTeamList = teamRepository.findAll(spec, pageRequest);

        assertThat(findTeamList.getTotalPages()).isEqualTo(2);
    }

    @Test
    void 팀_조회_성공__유저_아이디가_일치하는_경우() {
        팀_생성("name", "description", 5L, user);

        Optional<Team> findTeam = teamRepository.findByUser_Id(user.getId());

        assertThat(findTeam).isNotNull();
        assertThat(findTeam.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void 팀_조회_실패__유저_아이디가_일치하지_않는_경우() {
        팀_생성("name", "description", 5L, user);

        Optional<Team> findTeam = teamRepository.findByUser_Id(2L);

        assertThat(findTeam).isEmpty();
    }

    private void 팀_생성(String name, String description, Long memberNum, User user) {
        team = teamRepository.save(Team.create(name, description, memberNum, user));
    }

    private void 팀_목록_생성(String name, String description, Long memberNum, User user) {
        for (int i = 1; i <= 20; i++) {
            Team team = Team.create(name + 1, description + i, memberNum, user);
            teamRepository.save(team);
        }
    }
}
