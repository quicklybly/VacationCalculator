package com.quicklybly.vacationcalculator.exception;

//TODO refactor exception
public class IntegrationException extends RuntimeException {
    private static final String MESSAGE = "Integration exception";

    public IntegrationException() {
        super(MESSAGE);
    }
}
