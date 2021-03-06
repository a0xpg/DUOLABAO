package com.hualong.duolabao.exception;

/**
 * Created by Administrator on 2019-07-15.
 */
public class ApiSysException extends Exception {

    private ExceptionEnum exceptionEnum;

    public ApiSysException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public ExceptionEnum getExceptionEnum() {
        return this.exceptionEnum;
    }

    public void setExceptionEnum(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public ApiSysException(String msg, Throwable t) {
        super(msg, t);
    }

    public ApiSysException(Throwable t) {
        super(t);
    }

    public ApiSysException(String msg) {
        super(msg);
    }

    public String toString() {
        return super.toString() + " " + this.exceptionEnum.toString();
    }
}

