package com.minecraft.job.integration.mail;

public enum MailTemplate {
    REVIEW_CREATE("[MCJ] 팀에 리뷰가 작성되었습니다.", "review_create.html");

    public String subject;
    public String code;

    MailTemplate(String subject, String code) {
        this.subject = subject;
        this.code = code;
    }
}
