package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validator.ValidAction;

import java.time.LocalDateTime;

/**
 * Класс описания запроса вещи
 */
@Data
public class ItemRequest {
    @NotNull(groups = {ValidAction.OnUpdate.class}, message = "id должен быть определен")
    private Long id;

    @Size(max = 200, message = "Максимальная длина описания - 200 символов.",
            groups = {ValidAction.OnCreate.class, ValidAction.OnUpdate.class})
    private String description;

    private User requestor;
    private LocalDateTime created;
}
