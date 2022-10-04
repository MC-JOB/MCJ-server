package com.minecraft.job.common.resume.domain;

import com.minecraft.job.common.fixture.ResumeFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.minecraft.job.common.resume.domain.ResumeStatue.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;

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

    @Test
    void 이력서_수정_성공() {
        Resume resume = ResumeFixture.create(user);

        resume.update("updateTitle", "updateContent", "updateTrainingHistory");

        assertThat(resume.getTitle()).isEqualTo("updateTitle");
        assertThat(resume.getContent()).isEqualTo("updateContent");
        assertThat(resume.getTrainingHistory()).isEqualTo("updateTrainingHistory");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이력서_수정_실패__title이_널이나_공백(String title) {
        Resume resume = ResumeFixture.create(user);

        assertThatIllegalArgumentException().isThrownBy(() -> resume.update(title, "content", "trainingHistory"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이력서_수정_실패__content가_널이나_공백(String content) {
        Resume resume = ResumeFixture.create(user);

        assertThatIllegalArgumentException().isThrownBy(() -> resume.update("title", content, "trainingHistory"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이력서_수정_실패__trainingHistory가_널이나_공백(String trainingHistory) {
        Resume resume = ResumeFixture.create(user);

        assertThatIllegalArgumentException().isThrownBy(() -> resume.update("title", "content", trainingHistory));
    }

    @Test
    void 이력서_수정_실패__삭제됨_상태임() {
        Resume resume = ResumeFixture.create(user);

        resume.setStatus(DELETED);

        assertThatIllegalStateException().isThrownBy(() -> resume.update("title", "content", "trainingHistory"));
    }

    @Test
    void 이력서_비활성화_성공() {
        Resume resume = ResumeFixture.create(user);

        resume.inactivate();

        assertThat(resume.getStatus()).isEqualTo(INACTIVATED);
    }

    @ParameterizedTest
    @EnumSource(value = ResumeStatue.class, names = {"INACTIVATED", "DELETED"}, mode = INCLUDE)
    void 이력서_비활성화_실패__비활성화_가능한_상태가_아님(ResumeStatue statue) {
        Resume resume = ResumeFixture.create(user);

        resume.setStatus(statue);

        assertThatIllegalStateException().isThrownBy(resume::inactivate);
    }

    @Test
    void 이력서_활성화_성공() {
        Resume resume = ResumeFixture.create(user);

        resume.activate();

        assertThat(resume.getStatus()).isEqualTo(ACTIVATED);
    }

    @ParameterizedTest
    @EnumSource(value = ResumeStatue.class, names = {"ACTIVATED", "DELETED"}, mode = INCLUDE)
    void 이력서_활성화_실패__활성화_가능한_상태가_아님(ResumeStatue statue) {
        Resume resume = ResumeFixture.create(user);

        resume.setStatus(statue);

        assertThatIllegalStateException().isThrownBy(resume::activate);
    }

    @Test
    void 이력서_삭제_성공() {
        Resume resume = ResumeFixture.create(user);

        resume.delete();

        assertThat(resume.getStatus()).isEqualTo(DELETED);
    }

    @Test
    void 이력서_삭제_실패__이미_삭제됨_상태임() {
        Resume resume = ResumeFixture.create(user);

        resume.delete();

        assertThatIllegalStateException().isThrownBy(resume::delete);
    }
}
