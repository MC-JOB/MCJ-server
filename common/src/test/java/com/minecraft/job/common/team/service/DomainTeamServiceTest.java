package com.minecraft.job.common.team.service;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@SpringBootTest
@Transactional
class DomainTeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void 팀_생성_성공() {
        Team team = teamService.create("name", "email", "password", "description", "logo", 5L);

        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();

        assertThat(findTeam.getId()).isNotNull();
    }

    @Test
    void 팀_생성_실패__이미_존재하는_이메일() {
        teamService.create("name", "email", "password", "description", "logo", 5L);

        assertThatIllegalStateException().isThrownBy(() -> teamService.create("name", "email", "password", "description", "logo", 5L));
    }
}
