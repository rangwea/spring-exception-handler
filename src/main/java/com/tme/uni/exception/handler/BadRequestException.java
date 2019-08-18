package com.tme.uni.exception.handler;

public class BadRequestException extends BusinessException {
    public BadRequestException(ErrorType error) {
        super(error);
    }

    public BadRequestException(ErrorType error, String detail) {
        super(error, detail);
    }
}
