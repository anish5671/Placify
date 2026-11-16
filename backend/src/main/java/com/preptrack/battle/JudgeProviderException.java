package com.preptrack.battle;

import org.springframework.http.HttpStatus;

public class JudgeProviderException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;

    public JudgeProviderException(HttpStatus httpStatus, String errorCode, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
