package com.minecraft.job.common.emailauth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
}
