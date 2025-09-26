package com.spedu.tutors.auth.exceptions;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String resource, String field, String value) {
        super(String.format("%s not found with %s: '%s'", resource, field, value));
    }
}

