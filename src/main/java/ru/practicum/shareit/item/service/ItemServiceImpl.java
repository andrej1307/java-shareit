package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.excepton.AccessDeniedException;
import ru.practicum.shareit.excepton.InternalServerException;
import ru.practicum.shareit.excepton.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    public ItemServiceImpl(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    @Override
    public Item addItem(Item item) {
        return itemStorage.addNewItem(item)
                .orElseThrow(() ->
                        new InternalServerException("Ошибка при добавления вещи : " + item.toString()));
    }

    @Override
    public Item updateItem(Item updItem) {
        Item item = itemStorage.getItemById(updItem.getId())
                .orElseThrow(() ->
                        new NotFoundException("Не найдена вещь id=" + updItem.getId()));
        if (!updItem.getOwner().equals(item.getOwner())) {
            throw new AccessDeniedException("Редактировать данные может только владелец вещи.");
        }

        if (updItem.getName() != null) {
            item.setName(updItem.getName());
        }
        if (updItem.getDescription() != null) {
            item.setDescription(updItem.getDescription());
        }
        if (updItem.getRequest() != null) {
            item.setRequest(updItem.getRequest());
        }
        if (updItem.getAvailable() != null) {
            item.setAvailable(updItem.getAvailable());
        }

        return itemStorage.updateItem(item)
                .orElseThrow(() ->
                        new InternalServerException("Ошибка при обновлении информации: " + updItem));
    }

    @Override
    public Item getItem(Long id) {
        return itemStorage.getItemById(id)
                .orElseThrow(() ->
                        new NotFoundException("Не найдена вещь id=" + id));
    }

    @Override
    public void deleteItem(Long id) {
        itemStorage.deleteItem(id);
    }

    @Override
    public String deleteAllItems() {
        itemStorage.deleteAllItems();
        return "Все вещи удалены.";
    }

    @Override
    public Collection<ItemDto> getItemsByOwnerId(User owner) {
        Collection<ItemDto> items;
        items = itemStorage.getAllItems().stream()
                .filter(item -> item.getOwner().equals(owner))
                .map(ItemMapper::toItemDto)
                .toList();
        return items;
    }

    @Override
    public Collection<ItemDto> searchItemsByText(final String text) {
        if (text == null || text.isEmpty()) {
            return List.of();
        }

        Collection<ItemDto> items;
        items = itemStorage.getAllItems().stream()
                .filter(item ->
                        item.getName().toUpperCase().contains(text.toUpperCase())
                                || item.getDescription().toUpperCase().contains(text.toUpperCase()))
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .toList();
        return items;
    }
}
