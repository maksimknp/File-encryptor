package com.mipt.app.exception;

public class SaveException extends RuntimeException{

    public static String FILE_SAVE_EXCEPTION = "File with path: %s does exist";

    public SaveException(String message) {
        super(message);
    }

    public SaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
