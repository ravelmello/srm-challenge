package com.ravel.teste.srm.utils;

public class ProductNotExistsException extends RuntimeException {
    public ProductNotExistsException(String message) {
        super(message);
    }

    public ProductNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
