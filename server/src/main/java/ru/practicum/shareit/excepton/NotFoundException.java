package ru.practicum.shareit.excepton;

/**
 * Класс исключения при отсутствии искомой информации
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
