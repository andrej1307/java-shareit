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
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId)
            throws AccessDeniedException, InternalServerException {
        User owner = userStorage.getUserById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден id=" + ownerId));

        Item newItem = ItemMapper.toItem(itemDto);
        newItem.setOwner(owner);
        Item savedItem = itemStorage.addNewItem(newItem)
                .orElseThrow(() ->
                        new InternalServerException("Ошибка при добавления вещи : " + newItem));
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(ItemDto updItemDto, Long ownerId) {
        User owner = userStorage.getUserById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден id=" + ownerId));

        Item updItem = ItemMapper.toItem(updItemDto);
        updItem.setOwner(owner);

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

        Item savedItem = itemStorage.updateItem(item)
                .orElseThrow(() ->
                        new InternalServerException("Ошибка при обновлении информации: " + updItem));
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto getItem(Long id) {
        Item item = itemStorage.getItemById(id)
                .orElseThrow(() ->
                        new NotFoundException("Не найдена вещь id=" + id));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public void deleteItem(Long id, Long ownerId) {
        Item item = itemStorage.getItemById(id)
                .orElseThrow(() ->
                        new NotFoundException("Не найдена вещь id=" + id));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("Удалять вещь может только хозяин.");
        }
        itemStorage.deleteItem(id);
    }

    @Override
    public Collection<ItemDto> deleteAllItems() {
        itemStorage.deleteAllItems();
        return List.of();
    }

    @Override
    public Collection<ItemDto> getItemsByOwnerId(Long ownerId) {
        User owner = userStorage.getUserById(ownerId)
                .orElseThrow(() ->
                        new NotFoundException("Владелец не найден. id=" + ownerId));
        Collection<ItemDto> items;
        items = itemStorage.getAllItemsByOwner(owner).stream()
                .map(ItemMapper::toItemDto)
                .toList();
        return items;
    }

    @Override
    public Collection<ItemDto> searchItemsByText(final String text) {
        return itemStorage.findItemsBytext(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
