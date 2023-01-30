package com.minecraft.job.common.emailauth.service;

import com.minecraft.job.common.emailauth.domain.EmailAuth;
import com.minecraft.job.common.emailauth.domain.EmailAuthIssueEvent;
import com.minecraft.job.common.emailauth.domain.EmailAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DomainEmailService implements EmailAuthService {

    private final EmailAuthRepository emailAuthRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void issue(String email) {
        EmailAuth emailAuth = emailAuthRepository
                .findByEmail(email)
                .orElse(EmailAuth.create(email));

        emailAuth.issue();

        emailAuthRepository.save(emailAuth);

        applicationEventPublisher.publishEvent(new EmailAuthIssueEvent(emailAuth.getId()));
    }

    @Override
    public boolean validate(String email, String code) {
        EmailAuth emailAuth = emailAuthRepository.findByEmail(email).orElseThrow();

        return emailAuth.validate(code);
    }
}
