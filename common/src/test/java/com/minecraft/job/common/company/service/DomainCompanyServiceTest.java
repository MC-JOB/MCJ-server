package com.minecraft.job.common.company.service;

import com.minecraft.job.common.company.domain.Company;
import com.minecraft.job.common.company.domain.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DomainCompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void 회사_생성_성공() {
        Company company = companyService.create("email", "password", "name", "description", "logo");

        Company findCompany = companyRepository.getById(company.getId());

        assertThat(findCompany.getId()).isNotNull();
    }
}