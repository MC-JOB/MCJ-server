package com.minecraft.job.common.company.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class CompanyTest {

    @Test
    void 회사_생성_성공() {
        Company company = Company.create("email", "password", "name", "description", "logo");

        assertThat(company.getEmail()).isEqualTo("email");
        assertThat(company.getPassword()).isEqualTo("password");
        assertThat(company.getName()).isEqualTo("name");
        assertThat(company.getDescription()).isEqualTo("description");
        assertThat(company.getLogo()).isEqualTo("logo");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회사_생성_실패_email이_빈값(String email) {
        assertThatIllegalArgumentException().isThrownBy(() -> Company.create(email, "password", "name", "description", "logo"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회사_생성_실패_password가_빈값(String password) {
        assertThatIllegalArgumentException().isThrownBy(() -> Company.create("email", password, "name", "description", "logo"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회사_생성_실패_name이_빈값(String name) {
        assertThatIllegalArgumentException().isThrownBy((() -> Company.create("email", "password", name, "description", "logo")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회사_생성_실패_description이_빈값(String description) {
        assertThatIllegalArgumentException().isThrownBy((() -> Company.create("email", "password", "name", description, "logo")));
    }

    @Test
    void 회사_수정_성공() {
        Company company = Company.create("email", "password", "name", "description", "logo");

        company.update("updateEmail", "updatePassword", "updateName", "updateDescription", "updateLogo");

        assertThat(company.getEmail()).isEqualTo("updateEmail");
        assertThat(company.getPassword()).isEqualTo("updatePassword");
        assertThat(company.getName()).isEqualTo("updateName");
        assertThat(company.getDescription()).isEqualTo("updateDescription");
        assertThat(company.getLogo()).isEqualTo("updateLogo");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회사_수정_실패_email이_빈값(String email) {
        Company company = Company.create("email", "password", "name", "description", "logo");

        assertThatIllegalArgumentException().isThrownBy((() -> company.update(email, "updatePassword", "updateName", "updateDescription", "updateLogo")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회사_수정_실패_password가_빈값(String password) {
        Company company = Company.create("email", "password", "name", "description", "logo");

        assertThatIllegalArgumentException().isThrownBy((() -> company.update("updateEmail", password, "updateName", "updateDescription", "updateLogo")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회사_수정_실패_name이_빈값(String name) {
        Company company = Company.create("email", "password", "name", "description", "logo");

        assertThatIllegalArgumentException().isThrownBy((() -> company.update("updateEmail", "updatePassword", name, "updateDescription", "updateLogo")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회사_수정_실패_description이_빈값(String description) {
        Company company = Company.create("email", "password", "name", "description", "logo");

        assertThatIllegalArgumentException().isThrownBy((() -> company.update("updateEmail", "updatePassword", "updateName", description, "updateLogo")));
    }
}
