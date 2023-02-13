package com.minecraft.job.common.recruitmentProcess.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruitmentProcessRepository extends JpaRepository<RecruitmentProcess, Long> {

    Optional<RecruitmentProcess> findByUser_Id(Long userId);
}
