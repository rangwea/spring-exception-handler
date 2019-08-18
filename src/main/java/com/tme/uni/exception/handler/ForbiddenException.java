package com.tme.uni.exception.handler;

public class ForbiddenException extends BusinessException {
    public ForbiddenException(ErrorType error) {
        super(error);
    }

    public ForbiddenException(ErrorType error, String detail) {
        super(error, detail);
    }
}
