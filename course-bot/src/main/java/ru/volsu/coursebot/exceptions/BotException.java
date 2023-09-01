package ru.volsu.coursebot.exceptions;

public class BotException extends Exception {

    public BotException(String message) {
        super(message);
    }

    public BotException(Throwable cause) {
        super(cause);
    }
}
