package com.minecraft.job.api.controller;

import com.minecraft.job.api.security.user.McjUserException;
import com.minecraft.job.common.support.MinecraftJobException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApiAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ErrorResponse exception(Exception ex) {
        return new ErrorResponse(new ErrorData("에러가 발생했습니다."));
    }

    @ExceptionHandler(MinecraftJobException.class)
    public ErrorResponse exception(MinecraftJobException ex) {
        final String message = messageSource.getMessage(ex.getMessage(), null, Locale.getDefault());

        return new ErrorResponse(new ErrorData(message));
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(McjUserException.class)
    public ErrorResponse exception(McjUserException ex) {
        final String message = ex.getMessage();

        return new ErrorResponse(new ErrorData(message));
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        private ErrorData error;
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorData {
        private String message;
    }
}
