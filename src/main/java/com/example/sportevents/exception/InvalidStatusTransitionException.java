package com.example.sportevents.exception;

public class InvalidStatusTransitionException extends ApplicationException {
    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}
