package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCommentsDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequestDto request;
    private Instant lastBooking;
    private Instant nextBooking;
    List<CommentDto> comments;
}
