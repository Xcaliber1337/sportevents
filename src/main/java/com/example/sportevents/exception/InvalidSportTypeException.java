package com.example.sportevents.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSportTypeException extends ApplicationException {
    public InvalidSportTypeException(String message) {
        super(message);
    }
}
