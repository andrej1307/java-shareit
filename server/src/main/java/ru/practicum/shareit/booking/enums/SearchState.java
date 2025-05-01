package ru.practicum.shareit.booking.enums;

import java.util.Optional;

/**
 * Описание режимов поиска запросов на бронирование
 */
public enum SearchState {
    // Все
    ALL,
    // Текущие
    CURRENT,
    // Будущие
    FUTURE,
    // Завершенные
    PAST,
    // Отклоненные
    REJECTED,
    // Ожидающие подтверждения
    WAITING;

    public static Optional<SearchState> from(String stringState) {
        for (SearchState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
