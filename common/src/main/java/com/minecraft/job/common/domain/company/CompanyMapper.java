package com.minecraft.job.common.domain.company;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CompanyMapper {

    int create(CompanyVo companyVo);

    int update(CompanyVo companyVo);

    CompanyVo getCompany(int companyId);

    List<CompanyVo> getCompanys();
}
