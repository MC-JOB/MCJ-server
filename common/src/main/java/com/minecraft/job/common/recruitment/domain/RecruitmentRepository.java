package com.minecraft.job.common.recruitment.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, JpaSpecificationExecutor<Recruitment> {

    Page<Recruitment> findAll(Specification spec, Pageable pageable);
}
