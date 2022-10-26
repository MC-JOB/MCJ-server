package com.minecraft.job.api.service;

import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitmentProcess.domain.*;
import com.minecraft.job.integration.mail.Mail;
import com.minecraft.job.integration.mail.MailApi;
import com.minecraft.job.integration.mail.MailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultRecruitmentProcessAppService implements RecruitmentProcessAppService {

    private final RecruitmentProcessRepository recruitmentProcessRepository;
    private final MailApi mailApi;

    @Async
    @TransactionalEventListener(RecruitmentProcessCreateEvent.class)
    public void onCreateRecruitmentProcessListener(RecruitmentProcessCreateEvent event) throws Exception {
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(event.recruitmentProcessId()).orElseThrow();
        Recruitment recruitment = recruitmentProcess.getRecruitment();

        String recruitmentName = recruitment.getTitle();
        String teamName = recruitment.getTeamName();
        String leaderEmail = recruitment.getTeamOfLeaderEmail();
        String userNickname = recruitmentProcess.getUserNickname();

        mailApi.send(new Mail(
                new String[]{leaderEmail},
                MailTemplate.RECRUITMENT_PROCESS_CREATE,
                Map.of("teamName", teamName, "userNickname", userNickname,
                        "recruitmentName", recruitmentName)
        ));
    }

    @Async
    @TransactionalEventListener(RecruitmentProcessInProgressEvent.class)
    public void onCreateRecruitmentProcessListener(RecruitmentProcessInProgressEvent event) throws Exception {
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(event.recruitmentProcessId()).orElseThrow();
        Recruitment recruitment = recruitmentProcess.getRecruitment();

        String recruitmentName = recruitment.getTitle();
        String userEmail = recruitmentProcess.getUserEmail();
        String userNickname = recruitmentProcess.getUserNickname();

        mailApi.send(new Mail(
                new String[]{userEmail},
                MailTemplate.RECRUITMENT_PROCESS_INPROGRESS,
                Map.of("userNickname", userNickname,
                        "recruitmentName", recruitmentName)
        ));
    }

    @Async
    @TransactionalEventListener(RecruitmentProcessFailEvent.class)
    public void onCreateRecruitmentProcessListener(RecruitmentProcessFailEvent event) throws Exception {
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(event.recruitmentProcessId()).orElseThrow();
        Recruitment recruitment = recruitmentProcess.getRecruitment();

        String recruitmentName = recruitment.getTitle();
        String userEmail = recruitmentProcess.getUserEmail();
        String userNickname = recruitmentProcess.getUserNickname();

        mailApi.send(new Mail(
                new String[]{userEmail},
                MailTemplate.RECRUITMENT_PROCESS_FAIL,
                Map.of("userNickname", userNickname,
                        "recruitmentName", recruitmentName)
        ));
    }
}
