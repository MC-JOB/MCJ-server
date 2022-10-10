package com.minecraft.job.integration.mail;

import java.util.Map;

public record Mail(
        String[] to,
        MailTemplate mailTemplate,
        Map<String, Object> values
) {
}
