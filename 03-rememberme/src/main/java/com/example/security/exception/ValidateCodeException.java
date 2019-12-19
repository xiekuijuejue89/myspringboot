package com.example.security.exception;


import org.springframework.security.core.AuthenticationException;

public class ValidateCodeException extends AuthenticationException {
    private static final long serialVersionUID = 3549461846L;
    public  ValidateCodeException(String message){
        super(message);
    }
}
