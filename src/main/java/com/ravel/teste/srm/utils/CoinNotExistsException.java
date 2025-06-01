package com.ravel.teste.srm.utils;

public class CoinNotExistsException extends RuntimeException {
    public CoinNotExistsException(String message) {
        super(message);
    }

    public CoinNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoinNotExistsException() {

    }
}
