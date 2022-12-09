package org.example.dispenser.exception;

public class NoChangeException extends RuntimeException{
    private String message;
    public NoChangeException(String message) {
        this.message = message;
    }
}
