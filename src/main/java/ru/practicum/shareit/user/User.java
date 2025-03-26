package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.validator.ValidAction;

/**
 * Класс описания пользователя
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"email"})
public class User {
    private Long id;

    @NotBlank(message = "Имя не может быть пустым", groups = {ValidAction.OnCreate.class})
    private String name;

    @NotBlank(message = "Email не может быть пустым", groups = {ValidAction.OnCreate.class})
    @Email(message = "Email должен удовлетворять правилам формирования почтовых адресов.",
            groups = {ValidAction.OnCreate.class, ValidAction.OnUpdate.class})
    private String email;
}
