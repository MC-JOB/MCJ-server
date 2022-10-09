package com.minecraft.job.integration.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Component
@Profile({"dev", "prod"})
@RequiredArgsConstructor
public class JavaMailApi implements MailApi {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void send(Mail mail) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper
                = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());


        mimeMessageHelper.setTo(mail.to());
        mimeMessageHelper.setSubject(mail.mailTemplate().subject);

        Context context = new Context();
        context.setVariables(mail.values());

        String html = templateEngine.process(mail.mailTemplate().code, context);

        mimeMessageHelper.setText(html, true);

        javaMailSender.send(message);
    }
}
