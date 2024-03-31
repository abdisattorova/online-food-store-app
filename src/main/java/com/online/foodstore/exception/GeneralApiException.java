package com.online.foodstore.exception;

import lombok.Getter;


@Getter
public class GeneralApiException extends RuntimeException {
    private String errorCode;

    public GeneralApiException(String message) {
        super(message);
    }

    public GeneralApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
