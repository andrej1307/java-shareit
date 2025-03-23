package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validator.ValidAction;

@Data
@AllArgsConstructor
public class UserDto {
    @NotNull(groups = {ValidAction.OnUpdate.class}, message = "id должен быть определен")
    private Long id;

    @NotBlank(message = "Имя не может быть пустым", groups = {ValidAction.OnCreate.class})
    private String name;

    @NotBlank(message = "Email не может быть пустым", groups = {ValidAction.OnCreate.class})
    @Email(message = "Email должен удовлетворять правилам формирования почтовых адресов.",
            groups = {ValidAction.OnCreate.class, ValidAction.OnUpdate.class})
    private String email;
}
