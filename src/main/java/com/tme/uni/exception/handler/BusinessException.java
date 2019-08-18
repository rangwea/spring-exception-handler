package com.tme.uni.exception.handler;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BusinessException extends RuntimeException {
    private ErrorType error;
    private String detail;

    public BusinessException(ErrorType error) {
        this.error = error;
    }

    public BusinessException(ErrorType error, String detail) {
        this.error = error;
        this.detail = detail;
    }
}
