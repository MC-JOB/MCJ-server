package com.minecraft.job.common.recruitment.domain;

import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.team.domain.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.minecraft.job.common.recruitment.domain.RecruitmentStatus.CREATED;
import static org.assertj.core.api.Assertions.*;

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
}
