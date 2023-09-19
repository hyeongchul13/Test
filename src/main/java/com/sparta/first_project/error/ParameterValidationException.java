package com.sparta.first_project.error;

public class ParameterValidationException extends RuntimeException {

    public ParameterValidationException(String message) {
        super(message);
    }
}
