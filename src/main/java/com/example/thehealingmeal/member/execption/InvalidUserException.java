package com.example.thehealingmeal.member.execption;

public class InvalidUserException extends RuntimeException{

    public InvalidUserException(final String message){
        super(message);
    }
}
