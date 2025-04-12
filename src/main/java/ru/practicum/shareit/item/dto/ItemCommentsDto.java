package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCommentsDto {
    List<CommentDto> comments;
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long request;
    private Instant lastBooking;
    private Instant nextBooking;
}
