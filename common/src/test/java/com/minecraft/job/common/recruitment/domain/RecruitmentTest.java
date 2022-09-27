package com.minecraft.job.common.recruitment.domain;

import com.minecraft.job.common.fixture.RecruitmentFixture;
import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.team.domain.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDateTime;

import static com.minecraft.job.common.recruitment.domain.RecruitmentStatus.ACTIVATED;
import static com.minecraft.job.common.recruitment.domain.RecruitmentStatus.CREATED;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;

class RecruitmentTest {

    private Team team;

    @BeforeEach
    void setUp() {
        team = TeamFixture.create();
    }

    @Test
    void 채용공고_생성_성공() {
        Recruitment recruitment = Recruitment.create("title", "content", team);

        assertThat(recruitment.getTitle()).isEqualTo("title");
        assertThat(recruitment.getContent()).isEqualTo("content");
        assertThat(recruitment.getTeam()).isEqualTo(team);
        assertThat(recruitment.getStatus()).isEqualTo(CREATED);
        assertThat(recruitment.getCreatedAt()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 채용공고_생성_실패__title이_널이거나_공백(String title) {
        assertThatIllegalArgumentException().isThrownBy(() -> Recruitment.create(title, "content", team));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 채용공고_생성_실패__content가_널이거나_공백(String content) {
        assertThatIllegalArgumentException().isThrownBy(() -> Recruitment.create("title", content, team));
    }

    @Test
    void 채용공고_생성_실패__team이_널() {
        assertThatNullPointerException().isThrownBy(() -> Recruitment.create("title", "content", null));
    }

    @Test
    void 채용공고_활성화_성공() {
        Recruitment recruitment = RecruitmentFixture.create();

        recruitment.activate(LocalDateTime.now().plusMinutes(1));

        assertThat(recruitment.getStatus()).isEqualTo(ACTIVATED);
        assertThat(recruitment.getClosedAt()).isNotNull();
    }

    @Test
    void 채용공고_활성화_실패__마감일이_현재보다_과거() {
        Recruitment recruitment = RecruitmentFixture.create();

        assertThatIllegalArgumentException().isThrownBy(() -> recruitment.activate(LocalDateTime.now().minusSeconds(1)));
    }

    @ParameterizedTest
    @EnumSource(value = RecruitmentStatus.class, names = {"ACTIVATED", "DELETED"}, mode = INCLUDE)
    void 채용공고_활성화_실패__활성화_가능한_상태아님(RecruitmentStatus status) {
        Recruitment recruitment = RecruitmentFixture.create();

        recruitment.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> recruitment.activate(LocalDateTime.now().plusMinutes(1)));
    }
}
