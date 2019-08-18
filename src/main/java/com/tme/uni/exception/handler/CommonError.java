package com.tme.uni.exception.handler;

public enum CommonError implements ErrorType {
    REQUEST_ERROR("000001", "request error"),
    SERVER_ERROR("000002", "server error")

    ;
    private String code;
    private String message;

    CommonError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
