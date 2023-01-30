package com.minecraft.job.api.service;

import com.minecraft.job.common.emailauth.domain.EmailAuth;
import com.minecraft.job.common.emailauth.domain.EmailAuthIssueEvent;
import com.minecraft.job.common.emailauth.domain.EmailAuthRepository;
import com.minecraft.job.integration.mail.Mail;
import com.minecraft.job.integration.mail.MailApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

import static com.minecraft.job.integration.mail.MailTemplate.EMAIL_AUTH_ISSUE;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultEmailAuthAppService implements EmailAuthAppService {

    private final EmailAuthRepository emailAuthRepository;
    private final MailApi mailApi;

    @EventListener(EmailAuthIssueEvent.class)
    public void onIssueEmailAuthListener(EmailAuthIssueEvent event) throws Exception {
        EmailAuth emailAuth = emailAuthRepository.findById(event.emailAuthId()).orElseThrow();

        String email = emailAuth.getEmail();
        String code = emailAuth.getCode();

        mailApi.send(new Mail(
                new String[]{email},
                EMAIL_AUTH_ISSUE,
                Map.of("code", code)
        ));
    }
}
