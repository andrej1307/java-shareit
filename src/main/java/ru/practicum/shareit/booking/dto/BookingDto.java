package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validator.ValidAction;

import java.time.LocalDateTime;

/**
 * DTO класс бронирования
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long Id;

    @NotNull(message = "Время начала бронирования должно быть задано.", groups = {ValidAction.OnCreate.class})
    private LocalDateTime timeStart;

    @NotNull(message = "Время завершения бронирования должно быть задано.", groups = {ValidAction.OnCreate.class})
    private LocalDateTime timeEnd;

    @NotNull(message = "Вещь для бронирования должна быть определена.", groups = {ValidAction.OnCreate.class})
    private ItemDto itemDto;

    @NotNull(message = "Заказчик бронирования должна быть указан.", groups = {ValidAction.OnCreate.class})
    private UserDto userDto;

    private BookingStatus status = BookingStatus.WAITING;
}
