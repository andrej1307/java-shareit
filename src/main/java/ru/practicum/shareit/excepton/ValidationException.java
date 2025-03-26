package ru.practicum.shareit.excepton;

/**
 * Класс исключений при проверке допустимых значений переменных
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
