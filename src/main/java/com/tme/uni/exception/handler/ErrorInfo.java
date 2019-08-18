package com.tme.uni.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
public class ErrorInfo {
    private String code;
    private String message;
    private String detail;
    private String url;
    private String date;

    public ErrorInfo() { }
}
