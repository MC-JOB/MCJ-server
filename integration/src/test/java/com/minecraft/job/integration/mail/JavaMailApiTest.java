package com.minecraft.job.integration.mail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class JavaMailApiTest {

    @Autowired
    private MailApi mailApi;

    @Test
    void 메일_발송_성공() throws Exception {
        Mail mail = new Mail(new String[]{"sksqkrwns@naver.com"}, MailTemplate.TEST, Map.of("name", "jun"));

        mailApi.send(mail);
    }
}
