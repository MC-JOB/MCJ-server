package com.minecraft.job.common.resume.domain;

import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.minecraft.job.common.resume.domain.ResumeStatue.CREATED;
import static org.assertj.core.api.Assertions.*;

class ResumeTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = UserFixture.create();
    }

    @Test
    void 이력서_생성_성공() {
        Resume resume = Resume.create("title", "content", "trainingHistory", user);

        assertThat(resume.getTitle()).isEqualTo("title");
        assertThat(resume.getContent()).isEqualTo("content");
        assertThat(resume.getTrainingHistory()).isEqualTo("trainingHistory");
        assertThat(resume.getUser()).isEqualTo(user);
        assertThat(resume.getStatus()).isEqualTo(CREATED);
        assertThat(resume.getCreatedAt()).isNotNull();
    }

    @Test
    void 이력서_생성_실패_user가_널() {
        assertThatNullPointerException().isThrownBy(() -> Resume.create("title", "content", "trainingHistory", null));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이력서_생성_실패__title이_널이나_공백(String title) {
        assertThatIllegalArgumentException().isThrownBy(() -> Resume.create(title, "content", "trainingHistory", user));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이력서_생성_실패__content가_널이나_공백(String content) {
        assertThatIllegalArgumentException().isThrownBy(() -> Resume.create("title", content, "trainingHistory", user));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이력서_생성_실패__trainingHistory가_널이나_공백(String trainingHistory) {
        assertThatIllegalArgumentException().isThrownBy(() -> Resume.create("title", "content", trainingHistory, user));
    }
}
