package com.seplag.employee_manager.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -828417283420562726L;

    public ResourceNotFoundException(final String ex) {
        super(ex);
    }

}