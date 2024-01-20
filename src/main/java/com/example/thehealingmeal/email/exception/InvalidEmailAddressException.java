package com.example.thehealingmeal.email.exception;

public class InvalidEmailAddressException extends RuntimeException{

    public InvalidEmailAddressException(String message) {
        super(message);
    }

    public InvalidEmailAddressException() {
        this("이메일 형식이 올바르지 않습니다.");
    }
}
