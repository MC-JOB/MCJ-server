package com.minecraft.job.common.company.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void 회사_생성_성공() {
        Company company = Company.create("email", "password", "name", "description", "logo");

        companyRepository.save(company);

        Company findCompany = companyRepository.findById(company.getId()).orElseThrow();

        assertThat(findCompany.getId()).isNotNull();
        assertThat(findCompany.getEmail()).isEqualTo("email");
        assertThat(findCompany.getPassword()).isEqualTo("password");
        assertThat(findCompany.getName()).isEqualTo("name");
        assertThat(findCompany.getDescription()).isEqualTo("description");
        assertThat(findCompany.getLogo()).isEqualTo("logo");
        assertThat(findCompany.getScore()).isEqualTo(0);
        assertThat(findCompany.getCreateAt()).isNotNull();
    }
}
