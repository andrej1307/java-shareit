package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validator.ValidAction;

/**
 * Клас описания вещи для совместного использования
 */
@Data
@EqualsAndHashCode(of = {"name"})
public class Item {
    @NotNull(groups = {ValidAction.OnUpdate.class}, message = "id должен быть определен")
    private Long id;

    @NotBlank(message = "Имя не может быть пустым", groups = {ValidAction.OnCreate.class})
    private String name;

    @Size(min = 0, max = 200, message = "Максимальная длина описания - 200 символов.",
            groups = {ValidAction.OnCreate.class, ValidAction.OnUpdate.class})
    private String description;

    private boolean available;
    private User owner;
    private ItemRequest request;
}
