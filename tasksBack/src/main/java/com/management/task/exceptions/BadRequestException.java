package com.management.task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1708664733842323966L;

    public BadRequestException(final String message) {
        super(message);
    }

    public BadRequestException(final String message, final Throwable e) {
        super(message, e);
    }
}
