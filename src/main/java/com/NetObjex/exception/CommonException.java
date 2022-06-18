package com.NetObjex.exception;

public class CommonException extends RuntimeException {

    public CommonException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
