package com.minecraft.job.common.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ALREADY_USED_EMAIL("error.already.used.email"),
    NOT_SAME_PASSWORD("error.not.same.password"),
    CODE_NOT_VALID("error.code.not.valid"),
    TRY_COUNT_EXCEEDED("error.try.count.exceeded"),
    CODE_VALID_TIME_EXCEEDED("error.code.valid.time.exceeded");

    private final String message;
}
