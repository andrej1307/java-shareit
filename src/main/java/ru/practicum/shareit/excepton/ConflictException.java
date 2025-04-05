package ru.practicum.shareit.excepton;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
