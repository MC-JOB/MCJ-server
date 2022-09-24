package com.minecraft.job.common.domain.company;

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
    private CompanyMapper companyMapper;

    @Test
    void 회사_생성_성공() {
        companyService.create("email", "password", "name", "description", "logo");

        CompanyVo findCompany = companyMapper.getCompanys().get(0);

        assertThat(findCompany.getId()).isNotNull();
    }
}