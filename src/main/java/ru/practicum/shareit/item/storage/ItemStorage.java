package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {
    Optional<Item> addNewItem(Item item);

    Optional<Item> getItemById(Long id);

    Optional<Item> updateItem(Item item);

    void deleteItem(Long id);

    Collection<Item> getAllItems();

    Collection<Item> getAllItemsByOwner(User owner);

    Collection<Item> findItemsBytext(String text);

    String deleteAllItems();
}
