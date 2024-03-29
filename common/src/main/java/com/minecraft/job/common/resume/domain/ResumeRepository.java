package com.minecraft.job.common.resume.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Page<Resume> findAll(Specification spec, Pageable pageable);

    Optional<Resume> findByUser_id(Long userId);

    Resume findByTitle(String searchName);
}
