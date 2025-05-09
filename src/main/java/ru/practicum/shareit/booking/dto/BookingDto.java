package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.enums.BookingStatus;
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
    private Long id;

    @NotNull(message = "Время начала бронирования должно быть задано.", groups = {ValidAction.OnCreate.class})
    private LocalDateTime start;

    @NotNull(message = "Время завершения бронирования должно быть задано.", groups = {ValidAction.OnCreate.class})
    private LocalDateTime end;

    private Long itemId;
    private ItemDto item;

    private Long bookerId;
    private UserDto booker;

    private BookingStatus status = BookingStatus.WAITING;
}
