package com.mipt.app.exception;

public class RegisterException extends RuntimeException {

    public static String EXIST_USERNAME = "User with username %s does exist";
    public static String BAD_KEY_FILE = "File with path: %s doesn't exist";

    public RegisterException(String message) {
        super(message);
    }

    public RegisterException(String message, Throwable cause) {
        super(message, cause);
    }
}
