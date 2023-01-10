package com.minecraft.job.common.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ALREADY_USED_EMAIL("error.already.used.email"),
    NOT_SAME_PASSWORD("error.not.same.password");

    private final String message;
}
