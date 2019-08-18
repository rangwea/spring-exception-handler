package com.tme.uni.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo notFoundErrorHandler(HttpServletRequest req, BadRequestException e) {
        return this.businessErrorHandler(req, e);
    }

    @ExceptionHandler(value = ConflictException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorInfo conflictErrorHandler(HttpServletRequest req, ConflictException e) {
        return this.businessErrorHandler(req, e);
    }

    @ExceptionHandler(value = ForbiddenException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorInfo forbiddenErrorHandler(HttpServletRequest req, ForbiddenException e) {
        return this.businessErrorHandler(req, e);
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorInfo forbiddenErrorHandler(HttpServletRequest req, NotFoundException e) {
        return this.businessErrorHandler(req, e);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorInfo unauthorizedErrorHandler(HttpServletRequest req, UnauthorizedException e) {
        return this.businessErrorHandler(req, e);
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorInfo businessErrorHandler(HttpServletRequest req, BusinessException e) {
        ErrorInfo errorInfo = new ErrorInfo();

        errorInfo.setCode(e.getError().code());
        errorInfo.setMessage(e.getError().message());
        errorInfo.setDetail(e.getDetail());
        errorInfo.setDate(DATE_FORMAT.format(System.currentTimeMillis()));
        errorInfo.setUrl(req.getRequestURL().toString());

        log.warn("business exception:errorInfo={}", errorInfo, e);
        return errorInfo;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorInfo mvcExceptionHandler(HttpServletRequest req, HttpRequestMethodNotSupportedException e) {
        return mvcErrorInfo(req, e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorInfo mvcExceptionHandler(HttpServletRequest req, HttpMediaTypeNotSupportedException e) {
        return mvcErrorInfo(req, e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorInfo mvcExceptionHandler(HttpServletRequest req, HttpMediaTypeNotAcceptableException e) {
        return mvcErrorInfo(req, e.getMessage());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo mvcExceptionHandler(HttpServletRequest req, MissingPathVariableException e) {
        return mvcErrorInfo(req, e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo mvcExceptionHandler(HttpServletRequest req, MissingServletRequestParameterException e) {
        return mvcErrorInfo(req, e.getMessage());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo mvcExceptionHandler(HttpServletRequest req, MissingRequestHeaderException e) {
        return mvcErrorInfo(req, e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorInfo mvcExceptionHandler(HttpServletRequest req, NoHandlerFoundException e) {
        return mvcErrorInfo(req, e.getMessage());
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorInfo defaultErrorHandler(HttpServletRequest req, Exception e) {
        ErrorInfo errorInfo = new ErrorInfo();

        errorInfo.setCode(CommonError.SERVER_ERROR.code());
        errorInfo.setMessage(CommonError.SERVER_ERROR.message());
        errorInfo.setDate(DATE_FORMAT.format(System.currentTimeMillis()));
        errorInfo.setUrl(req.getRequestURL().toString());

        log.error("server error:errorInfo={}", errorInfo, e);

        return errorInfo;
    }

    private ErrorInfo mvcErrorInfo(HttpServletRequest req, String detail) {
        return new ErrorInfo(CommonError.REQUEST_ERROR.code()
                , CommonError.REQUEST_ERROR.message()
                , detail, req.getRequestURL().toString()
                , DATE_FORMAT.format(System.currentTimeMillis())
        );
    }
}
