package ru.yandex.practicum.filmorate.exception;

public class ControllerException extends RuntimeException {
    public ControllerException(String message) {
        super(message);
    }
}