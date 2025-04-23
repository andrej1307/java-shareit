package ru.practicum.shareit.excepton;


public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
}
