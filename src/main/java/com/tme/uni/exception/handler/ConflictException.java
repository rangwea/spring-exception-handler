package com.tme.uni.exception.handler;

public class ConflictException extends BusinessException {
    public ConflictException(ErrorType error) {
        super(error);
    }

    public ConflictException(ErrorType error, String detail) {
        super(error, detail);
    }
}
