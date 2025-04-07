package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.excepton.AccessDeniedException;
import ru.practicum.shareit.excepton.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден id=" + ownerId));

        Item newItem = ItemMapper.toItem(itemDto);
        newItem.setOwner(owner);
        Item savedItem = itemRepository.save(newItem);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(ItemDto updItemDto, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден id=" + ownerId));

        Item updItem = ItemMapper.toItem(updItemDto);
        updItem.setOwner(owner);

        Item item = itemRepository.findById(updItem.getId())
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
        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto getItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Не найдена вещь id=" + id));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public void deleteItem(Long id, Long ownerId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Не найдена вещь id=" + id));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("Удалять вещь может только хозяин.");
        }
        itemRepository.delete(item);
    }

    @Override
    public Collection<ItemDto> deleteAllItems() {
        itemRepository.deleteAll();
        return List.of();
    }

    @Override
    public Collection<ItemDto> getItemsByOwnerId(Long ownerId) {
        Collection<ItemDto> items;
        items = itemRepository.findByOwnerIdEquals(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
        return items;
    }

    @Override
    public Collection<ItemDto> searchItemsByText(final String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        return itemRepository
                .findByNameOrDescriptionContainingIgnoreCase(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
