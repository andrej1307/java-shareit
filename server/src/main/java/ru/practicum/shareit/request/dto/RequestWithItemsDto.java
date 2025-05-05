package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RequestWithItemsDto {
    private Long id;
    private String description;
    private UserDto requestor;
    private Instant created;
    private List<ItemShortDto> items = new ArrayList<>();
}
