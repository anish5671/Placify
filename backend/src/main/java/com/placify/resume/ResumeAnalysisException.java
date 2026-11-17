package com.placify.resume;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResumeAnalysisException extends RuntimeException {
    private final HttpStatus status;
    private final String errorCode;

    public ResumeAnalysisException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}
