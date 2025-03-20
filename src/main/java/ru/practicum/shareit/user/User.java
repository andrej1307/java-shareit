package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.validator.Valid;

/**
 * Класс описания пользователя
 */
@Data
@EqualsAndHashCode(of = {"name", "email"})
public class User {
    @NotNull(groups = {Valid.OnUpdate.class}, message = "id должен быть определен")
    Long id;

    @NotBlank(message = "Имя не может быть пустым", groups = {Valid.OnCreate.class})
    private String name;

    @NotBlank(message = "Email не может быть пустым", groups = {Valid.OnCreate.class})
    @Email(message = "Email должен удовлетворять правилам формирования почтовых адресов.",
            groups = {Valid.OnCreate.class, Valid.OnUpdate.class})
    private String email;
}
