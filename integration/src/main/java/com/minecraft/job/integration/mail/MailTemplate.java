package com.minecraft.job.integration.mail;

public enum MailTemplate {
    REVIEW_CREATE("[MCJ] 팀에 리뷰가 작성되었습니다.", "review_create.html"),
    RECRUITMENT_PROCESS_CREATE("[MCJ] 팀의 채용공고에 새로운 이력서가 도착했습니다.", "recruitmentProcess_create.html");

    public String subject;
    public String code;

    MailTemplate(String subject, String code) {
        this.subject = subject;
        this.code = code;
    }
}
