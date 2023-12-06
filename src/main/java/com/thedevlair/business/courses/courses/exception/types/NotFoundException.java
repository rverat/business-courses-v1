package com.thedevlair.business.courses.courses.exception.types;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}
