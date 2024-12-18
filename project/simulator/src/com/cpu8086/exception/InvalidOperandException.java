package com.cpu8086.exception;
public class InvalidOperandException extends CPUException {

    public InvalidOperandException(String message) {
        super(message);
    }

    public InvalidOperandException(String message, Throwable cause) {
        super(message, cause);
    }
}