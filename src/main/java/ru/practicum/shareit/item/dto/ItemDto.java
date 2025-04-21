package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.validator.ValidAction;

import java.time.Instant;

/**
 * Класс описания представления объектов при обмене данными
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(message = "Имя не может быть пустым", groups = {ValidAction.OnCreate.class})
    private String name;

    @NotBlank(message = "Описание не может быть пустым", groups = {ValidAction.OnCreate.class})
    @Size(max = 200, message = "Максимальная длина описания - 200 символов.",
            groups = {ValidAction.OnCreate.class, ValidAction.OnUpdate.class})
    private String description;

    @NotNull(message = "Доступ должен быть определен.", groups = {ValidAction.OnCreate.class})
    private Boolean available;

    private ItemRequestDto request;

    private Instant lastBooking;
    private Instant nextBooking;
}
