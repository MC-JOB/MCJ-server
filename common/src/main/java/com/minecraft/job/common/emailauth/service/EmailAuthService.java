package com.minecraft.job.common.emailauth.service;

public interface EmailAuthService {

    void issue(String email);

    boolean validate(String email, String code);
}
