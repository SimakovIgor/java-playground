package ru.simakov.com.exception;

public class ServiceException extends RuntimeException {

    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
