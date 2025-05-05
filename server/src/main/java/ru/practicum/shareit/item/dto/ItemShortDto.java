package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validator.ValidAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemShortDto {
    private Long id;
    private String name;
    private Long ownerId;
}
