package com.example.travelday.global.exception;

import com.example.travelday.global.exception.TokenException;

public class ExpiredTokenException extends TokenException {
    public ExpiredTokenException(String message) {
        super(message);
    }
}
