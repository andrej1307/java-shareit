package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validator.ValidAction;

/**
 * Клас описания вещи для совместного использования
 */
@Data
@EqualsAndHashCode(of = {"name", "description"})
@AllArgsConstructor
public class Item {
    @NotNull(groups = {ValidAction.OnUpdate.class}, message = "id должен быть определен")
    private Long id;

    @NotBlank(message = "Имя не может быть пустым", groups = {ValidAction.OnCreate.class})
    private String name;

    @Size(max = 200, message = "Максимальная длина описания - 200 символов.",
            groups = {ValidAction.OnCreate.class, ValidAction.OnUpdate.class})
    private String description;

    private Boolean available;
    private User owner;
    private ItemRequest request;

    public Item(Long id, String name, String description, Boolean available, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}
