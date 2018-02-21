package com.projectcarlton.fbljk.projectcarlton.API.Exception;

public class APIException {

    private int exceptionType;
    private String message;

    public APIException(int type, String message) {
        this.exceptionType = type;
        this.message = message;
    }

    public int getType() {
        return exceptionType;
    }

    public String getMessage() {
        return message;
    }
}