package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
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
            @RequestHeader("X-Sharer-User-Id") final Long ownerId) {
        log.info("Запрашиваем список вещей владельца id={}", ownerId);
        return itemService.getItemsByOwnerId(ownerId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto findItem(@PathVariable Long id) {
        log.info("Ищем вещь id={}.", id);
        return itemService.getItem(id);
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
            @RequestHeader("X-Sharer-User-Id") final Long ownerId,
            @Validated(ValidAction.OnCreate.class) @RequestBody ItemDto itemDto) {
        log.info("Пользователь id={} добавляет вещь : {}", ownerId, itemDto.toString());
        return itemService.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(
            @RequestHeader("X-Sharer-User-Id") final Long ownerId,
            @PathVariable Long id,
            @RequestBody ItemDto itemDto) {
        log.info("Пользователь id={} обновляет сведения об элемете id={} {}", ownerId, id, itemDto);
        itemDto.setId(id);
        return itemService.updateItem(itemDto, ownerId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItem(
            @RequestHeader("X-Sharer-User-Id") final Long ownerId,
            @PathVariable Long id) {
        log.info("пользователь id={}. Удаляет вещь id={}", ownerId, id);
        itemService.deleteItem(id, ownerId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public String deleteAllItems() {
        itemService.deleteAllItems();
        return "Все описания вещей удалены.";
    }
}
