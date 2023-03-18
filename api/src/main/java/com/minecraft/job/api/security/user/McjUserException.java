package com.minecraft.job.api.security.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class McjUserException extends RuntimeException {

    private String message;
    private HttpStatus status;

    public McjUserException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
