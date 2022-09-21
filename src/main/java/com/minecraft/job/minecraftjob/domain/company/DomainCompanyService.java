package com.minecraft.job.minecraftjob.domain.company;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class DomainCompanyService implements CompanyService {

    private final CompanyMapper companyMapper;

    @Override
    public void create(String email, String password, String name, String description, String logo) {
        CompanyVo company = CompanyVo.create(email, password, name, description, logo);

        companyMapper.create(company);
    }
}
