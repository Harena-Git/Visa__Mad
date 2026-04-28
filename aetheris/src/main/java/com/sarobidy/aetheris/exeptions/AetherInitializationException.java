package com.sarobidy.aetheris.exeptions;

public class AetherInitializationException extends Exception {
    public AetherInitializationException(String message) {
        super(message);
    }

    public AetherInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
