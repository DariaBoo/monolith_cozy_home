package com.cozyhome.onlineshop.exception;

public class DataAlreadyExistException extends RuntimeException{

    public DataAlreadyExistException(String message) {
        super(message);
    }
}
