package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public static NotFoundException of(String entityName, Long id) {
        return new NotFoundException(
                entityName + " not found with id: " + id
        );
    }

    public static NotFoundException of(
            String entityName,
            String fieldName,
            String value
    ) {
        return new NotFoundException(
                entityName + " not found with " + fieldName + ": " + value
        );
    }
}