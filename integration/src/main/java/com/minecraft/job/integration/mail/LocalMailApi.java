package com.minecraft.job.integration.mail;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "default"})
public class LocalMailApi implements MailApi {

    @Override
    public void send(Mail mail) throws Exception {

    }
}
