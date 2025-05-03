package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validator.ValidAction;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank(message = "Текст не может быть пустым", groups = {ValidAction.OnCreate.class})
    private String text;
    private Long authorId;
    private String authorName;
    private Long itemId;
    private Instant created;
}
