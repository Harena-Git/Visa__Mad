package com.sarobidy.aetheris.exeptions;


public class AetherException extends Exception {
    public AetherException(String message) {
        super(message);
    }

    public AetherException(String message, Throwable cause) {
        super(message, cause);
    }
}