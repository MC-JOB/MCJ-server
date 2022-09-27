package com.minecraft.job.common.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ALREADY_USED_PASSWORD("error.already.used.password");

    private final String message;
}
