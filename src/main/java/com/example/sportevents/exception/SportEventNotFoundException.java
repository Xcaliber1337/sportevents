package com.example.sportevents.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)

public class SportEventNotFoundException extends ApplicationException {
    public SportEventNotFoundException(String message) {
        super(message);
    }
}
