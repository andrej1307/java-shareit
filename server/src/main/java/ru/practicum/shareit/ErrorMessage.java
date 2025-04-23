package ru.practicum.shareit;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс сообщения об ошибке выполнения запроса
 */
@Getter
@AllArgsConstructor
public class ErrorMessage {
    private String error;
}
