package com.example.thehealingmeal.member.execption;

public class InvalidEmailAddressException extends RuntimeException{

    public InvalidEmailAddressException(String message){ super(message);}

    public InvalidEmailAddressException(){
        this("The email format is incorrect.");
    }
}
