package com.minecraft.job.common.company.service;


import com.minecraft.job.common.company.domain.Company;

public interface CompanyService {

    Company create(String email, String password, String name, String description, String logo);
}
