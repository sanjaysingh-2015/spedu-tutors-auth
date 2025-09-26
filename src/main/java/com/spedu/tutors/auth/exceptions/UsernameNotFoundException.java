package com.spedu.tutors.auth.exceptions;

public class UsernameNotFoundException  extends BaseException {
    public UsernameNotFoundException(String resource) {
        super(resource);
    }
}
