package com.cozyhome.onlineshop.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException (String message) {
        super(message);
    }
}
