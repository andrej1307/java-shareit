package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addItem(ItemDto item, Long ownerId);

    ItemDto updateItem(ItemDto updItem, Long ownerId);

    ItemCommentsDto getItem(Long id, Long userId);

    Collection<ItemDto> getItemsByOwnerId(Long ownerId);

    Collection<ItemDto> searchItemsByText(String text);

    void deleteItem(Long id, Long ownerId);

    Collection<ItemDto> deleteAllItems();
}
