package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface ItemService {
    Item addItem(Item item);

    Item updateItem(Item item);

    Item getItem(Long id);

    Collection<ItemDto> getItemsByOwnerId(User owner);

    Collection<ItemDto> searchItemsByText(String text);

    void deleteItem(Long id);

    String deleteAllItems();
}
