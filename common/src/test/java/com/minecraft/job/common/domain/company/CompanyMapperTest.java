package com.minecraft.job.common.domain.company;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CompanyMapperTest {


    @Autowired
    private CompanyMapper companyMapper;

    @Test
    void 회사_생성_성공() {
        CompanyVo company = CompanyVo.create("email", "password", "name", "description", "logo");

        int i = companyMapper.create(company);

        CompanyVo findCompany = companyMapper.getCompanys().get(0);

        assertThat(i).isEqualTo(1);
        assertThat(findCompany.getId()).isNotNull();
        assertThat(findCompany.getEmail()).isEqualTo("email");
        assertThat(findCompany.getPassword()).isEqualTo("password");
        assertThat(findCompany.getName()).isEqualTo("name");
        assertThat(findCompany.getDescription()).isEqualTo("description");
        assertThat(findCompany.getLogo()).isEqualTo("logo");
        assertThat(findCompany.getScore()).isEqualTo(0);
        assertThat(findCompany.getCreateAt()).isNotNull();
    }

    @Test
    void 회사_수정_성공() {
        CompanyVo company = CompanyVo.create("email", "password", "name", "description", "logo");

        companyMapper.create(company);

        CompanyVo findCompany = companyMapper.getCompanys().get(0);

        findCompany.update("updateEmail", "updatePassword", "upName", "updateDescription", "updateLogo");

        int updateResult = companyMapper.update(findCompany);

        findCompany = companyMapper.getCompanys().get(0);


        assertThat(updateResult).isEqualTo(1);
        assertThat(findCompany.getId()).isNotNull();
        assertThat(findCompany.getEmail()).isEqualTo("updateEmail");
        assertThat(findCompany.getPassword()).isEqualTo("updatePassword");
        assertThat(findCompany.getName()).isEqualTo("upName");
        assertThat(findCompany.getDescription()).isEqualTo("updateDescription");
        assertThat(findCompany.getLogo()).isEqualTo("updateLogo");
    }

    @Test
    void 회사_전체_조회_성공() {
        CompanyVo company1 = CompanyVo.create("email", "password", "name", "description", "logo");
        CompanyVo company2 = CompanyVo.create("email", "password", "name", "description", "logo");

        companyMapper.create(company1);
        companyMapper.create(company2);

        List<CompanyVo> findCompanys = companyMapper.getCompanys();

        assertThat(findCompanys.size()).isEqualTo(2);
    }
}
