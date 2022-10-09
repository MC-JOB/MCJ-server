package com.minecraft.job.integration.mail;

import org.springframework.context.annotation.Profile;

@Profile({"local", "default"})
public class LocalMailApi implements MailApi {

    @Override
    public void send(Mail mail) throws Exception {

    }
}
