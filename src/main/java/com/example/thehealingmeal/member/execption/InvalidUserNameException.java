package com.example.thehealingmeal.member.execption;

public class InvalidUserNameException extends RuntimeException {
    public InvalidUserNameException(final String message) { super(message);
    }
}
