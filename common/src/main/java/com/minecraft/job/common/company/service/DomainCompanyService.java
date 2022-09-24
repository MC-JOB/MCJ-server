package com.minecraft.job.common.company.service;

import com.minecraft.job.common.company.domain.Company;
import com.minecraft.job.common.company.domain.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class DomainCompanyService implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public Company create(String email, String password, String name, String description, String logo) {
        Company company = Company.create(email, password, name, description, logo);

        return companyRepository.save(company);
    }
}
