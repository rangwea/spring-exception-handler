package com.tme.uni.exception.handler;

public class NotFoundException extends BusinessException {
    public NotFoundException(ErrorType error) {
        super(error);
    }

    public NotFoundException(ErrorType error, String detail) {
        super(error, detail);
    }
}
