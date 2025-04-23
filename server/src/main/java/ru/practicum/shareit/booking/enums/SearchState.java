package ru.practicum.shareit.booking.enums;

/**
 * Описание режимов поиска запросов на бронирование
 */
public enum SearchState {
    ALL,        // Все
    CURRENT,    // Текущие
    PAST,       // Завершенные
    FUTURE,     // Будущие
    WAITING,    // Ожидающие подтверждения
    REJECTED,   // Отклоненные
}
