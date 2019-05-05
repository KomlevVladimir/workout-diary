package com.vladimirkomlev.workoutdiary.exception;

public class Error<T> {
    private ErrorCode errorCode;
    private T data;

    public Error(ErrorCode errorCode, T data) {
        this.errorCode = errorCode;
        this.data = data;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
