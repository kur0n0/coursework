package ru.volsu.coursebot.exceptions;

public class CoreException extends Exception {

    public CoreException(String message) {
        super(message);
    }

    public CoreException(Throwable cause) {
        super(cause);
    }
}
