package com.example.thehealingmeal.member.execption;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message){super(message);}
}
