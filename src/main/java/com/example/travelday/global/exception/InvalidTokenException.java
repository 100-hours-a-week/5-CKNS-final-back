package com.example.travelday.global.exception;

import com.example.travelday.global.exception.TokenException;

public class InvalidTokenException extends TokenException {

    public InvalidTokenException(String message) {
        super(message);
    }
}
