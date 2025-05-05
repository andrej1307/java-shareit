package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validator.ValidAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    @Email(message = "Email должен удовлетворять правилам формирования почтовых адресов.",
            groups = {ValidAction.OnCreate.class, ValidAction.OnUpdate.class})
    private String email;
}
