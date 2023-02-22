package dev.dobicinaitis.scavengerhunt.exceptions;

public class InvalidCodeException extends RuntimeException {
    public InvalidCodeException(String message) {
        super(message);
    }
}
