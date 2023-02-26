package com.minecraft.job.common.recruitmentProcess.domain;

import com.minecraft.job.common.resume.domain.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruitmentProcessRepository extends JpaRepository<RecruitmentProcess, Long> {

    Page<RecruitmentProcess> findAll(Specification spec, Pageable pageable);

    Optional<RecruitmentProcess> findByUser_Id(Long userId);
}
