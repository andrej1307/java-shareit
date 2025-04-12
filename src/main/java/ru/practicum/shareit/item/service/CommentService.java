package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(CommentDto commentDto);

    List<CommentDto> getCommentsByOwner(Long userId);

    List<CommentDto> getCommentsByItem(Long userId, Long itemId);
}
