package com.minecraft.job.integration.mail;

public enum MailTemplate {
    TEST("테스트입니다.", "test.html");

    public String subject;
    public String code;

    MailTemplate(String subject, String code) {
        this.subject = subject;
        this.code = code;
    }
}
