package com.minecraft.job.common.emailauth.service;

import com.minecraft.job.common.emailauth.domain.CodeNotValidException;
import com.minecraft.job.common.emailauth.domain.TimeExceededException;
import com.minecraft.job.common.emailauth.domain.TryCountExceededException;

public interface EmailAuthService {

    void issue(String email);

    boolean validate(String email, String code)
            throws TimeExceededException, TryCountExceededException, CodeNotValidException;
}
