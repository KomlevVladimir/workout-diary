package com.vladimirkomlev.workoutdiary.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String token, Throwable t) {
        super(token, t);
    }

    public InvalidTokenException(String token) {
        super(token);
    }
}
