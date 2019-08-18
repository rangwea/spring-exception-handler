package com.tme.uni.exception.handler;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(ErrorType error) {
        super(error);
    }

    public UnauthorizedException(ErrorType error, String detail) {
        super(error, detail);
    }
}
