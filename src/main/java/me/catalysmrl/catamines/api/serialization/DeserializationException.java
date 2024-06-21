package me.catalysmrl.catamines.api.serialization;

public class DeserializationException extends Exception {

    public DeserializationException() {
        super();
    }

    public DeserializationException(String message) {
        super(message);
    }

    public DeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

}
