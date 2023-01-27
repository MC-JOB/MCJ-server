package com.minecraft.job.common.emailauth.service;

import com.minecraft.job.common.emailauth.domain.EmailAuth;
import com.minecraft.job.common.emailauth.domain.EmailAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DomainEmailService implements EmailAuthService {

    private final EmailAuthRepository emailAuthRepository;

    @Override
    public void issue(String email) {
        EmailAuth emailAuth = emailAuthRepository
                .findByEmail(email)
                .orElse(EmailAuth.create(email));

        emailAuth.issue();

        emailAuthRepository.save(emailAuth);
    }
}
