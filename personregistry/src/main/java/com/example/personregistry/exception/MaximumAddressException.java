package com.example.personregistry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MaximumAddressException extends RuntimeException {

    public MaximumAddressException() {
        super();
    }

    public MaximumAddressException(String message) {
        super(message);
    }

    public MaximumAddressException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaximumAddressException(String resourceName, int maxAllowed, int currentCount) {
        super(String.format("%s can have maximum %d addresses. Current count: %d",
                resourceName, maxAllowed, currentCount));
    }
}