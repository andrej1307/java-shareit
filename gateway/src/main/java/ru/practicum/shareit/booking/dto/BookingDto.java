package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validator.ValidAction;

import java.time.LocalDateTime;

/**
 * DTO класс бронирования
 */
@Setter
@Getter
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

