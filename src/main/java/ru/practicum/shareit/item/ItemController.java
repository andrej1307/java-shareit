package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.excepton.AccessDeniedException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validator.ValidAction;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> findAllItems(
            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Запрашиваем список вещей владельца id={}", ownerId);
        User owner = userService.getUserById(ownerId);
        return itemService.getItemsByOwnerId(owner);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto findItem(@PathVariable Long id) {
        log.info("Ищем вещь id={}.", id);
        Item item = itemService.getItem(id);
        return ItemMapper.toItemDto(item);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> onSearch(@RequestParam String text) {
        log.info("Ищем вещи содержащие текст \"{}\" в названии или описании.", text);
        return itemService.searchItemsByText(text);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @Validated(ValidAction.OnCreate.class) @RequestBody ItemDto itemDto) {
        log.info("Добавляем вещь : {}", itemDto.toString());
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userService.getUserById(ownerId));

        return ItemMapper.toItemDto(itemService.addItem(item));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable Long id,
            @RequestBody ItemDto itemDto) {
        log.info("Пользователь id={}. Обновляет сведения об элемете id={} {}", ownerId, id, itemDto);
        Item item = ItemMapper.toItem(itemDto);
        item.setId(id);
        item.setOwner(userService.getUserById(ownerId));

        return ItemMapper.toItemDto(itemService.updateItem(item));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable Long id) {
        log.info("пользователь id={}. Удаляет вещь id={}", ownerId, id);
        Item item = itemService.getItem(id);
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("Удалять вещь может только хозяин.");
        }
        itemService.deleteItem(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public String deleteAllItems() {
        itemService.deleteAllItems();
        return "Все описания вещей удалены.";
    }
}
