package com.minecraft.job.common.support;

public class MinecraftJobException extends RuntimeException {

    public MinecraftJobException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public MinecraftJobException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
    }
}
