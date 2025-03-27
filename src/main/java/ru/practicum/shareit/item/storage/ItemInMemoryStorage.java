package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.excepton.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.*;

/**
 * Класс хранилища в памяти информации о вещах.
 */
@Repository
public class ItemInMemoryStorage implements ItemStorage {
    private final Map<Long, Item> items;
    private Long idMain = 1L;

    public ItemInMemoryStorage() {
        items = new HashMap<>();
    }

    @Override
    public Optional<Item> addNewItem(final Item item) {
        if (items.containsValue(item)) {
            Optional<Item> itemExists = items.values().stream()
                    .filter(item1 -> item1.equals(item))
                    .findFirst();
            if (itemExists.isPresent()) {
                throw new ValidationException("вещь уже существует: "
                        + itemExists.get());
            }
        }
        item.setId(idMain++);
        items.put(item.getId(), item);
        return Optional.of(items.get(item.getId()));
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        if (items.containsKey(id)) {
            return Optional.ofNullable(items.get(id));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Item> updateItem(Item item) {
        items.put(item.getId(), item);
        return Optional.ofNullable(items.get(item.getId()));
    }

    @Override
    public void deleteItem(Long id) {
        items.remove(id);
    }

    @Override
    public Collection<Item> getAllItems() {
        return items.values();
    }

    @Override
    public Collection<Item> getAllItemsByOwner(User owner) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(owner))
                .toList();
    }

    @Override
    public Collection<Item> findItemsBytext(final String text) {
        if (text == null || text.isEmpty()) {
            return List.of();
        }

        Collection<Item> findItems;
        findItems = items.values().stream()
                .filter(item ->
                        item.getName().toUpperCase().contains(text.toUpperCase())
                                || item.getDescription().toUpperCase().contains(text.toUpperCase()))
                .filter(Item::getAvailable)
                .toList();
        return findItems;
    }

    @Override
    public String deleteAllItems() {
        items.clear();
        return "Все вещи удалены";
    }
}
