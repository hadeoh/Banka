package com.usmanadio.banka.exceptions;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private String message;
    private HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
