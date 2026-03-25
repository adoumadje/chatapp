package com.adoumadje.chatapp.message.exception;

public class WrongMessageTargetException extends RuntimeException {
    public WrongMessageTargetException(String message) {
        super(message);
    }
}
