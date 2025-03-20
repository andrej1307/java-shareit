package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.validator.Valid;

/**
 * Клас описания вещи для совместного использования
 */
@Data
public class Item {
    @NotNull(groups = {Valid.OnUpdate.class}, message = "id должен быть определен")
    private Long id;

    @NotBlank(message = "Имя не может быть пустым", groups = {Valid.OnCreate.class})
    private String name;

    @Size(min = 0, max = 200, message = "Максимальная длина описания - 200 символов.",
            groups = {Valid.OnCreate.class, Valid.OnUpdate.class})
    private String description;

    private boolean available;
    private Long owner;
    private Long request ;
}
