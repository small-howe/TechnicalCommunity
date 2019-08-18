package com.tangwh.exception;

public class CustomizeException extends  RuntimeException {
    private String massage;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.code=errorCode.getCode();
        this.massage = errorCode.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return massage;
    }
}
