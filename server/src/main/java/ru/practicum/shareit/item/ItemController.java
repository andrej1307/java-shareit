package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * Класс обработки запросов на работу с вешью
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final ItemService itemService;
    private final CommentService commentService;

    public ItemController(ItemService itemService, CommentService commentService) {
        this.itemService = itemService;
        this.commentService = commentService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> findAllItems(
            @RequestHeader(HEADER_USER_ID) final Long ownerId) {
        log.info("Запрашиваем список вещей владельца id={}", ownerId);
        return itemService.getItemsByOwnerId(ownerId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemCommentsDto findItem(@PathVariable Long id,
                                    @RequestHeader(HEADER_USER_ID) final Long userId) {
        log.info("Польдователь id={} просматривает информацию о вещи id={}.", userId, id);
        return itemService.getItem(id, userId);
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
            @RequestHeader(HEADER_USER_ID) final Long ownerId,
            @RequestBody ItemDto itemDto) {
        log.info("Пользователь id={} добавляет вещь : {}", ownerId, itemDto.toString());
        return itemService.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(
            @RequestHeader(HEADER_USER_ID) final Long ownerId,
            @PathVariable Long id,
            @RequestBody ItemDto itemDto) {
        log.info("Пользователь id={} обновляет сведения об элемете id={} {}", ownerId, id, itemDto);
        itemDto.setId(id);
        return itemService.updateItem(itemDto, ownerId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItem(
            @RequestHeader(HEADER_USER_ID) final Long ownerId,
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

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(
            @RequestHeader(HEADER_USER_ID) final Long userId,
            @PathVariable Long itemId,
            @RequestBody CommentDto commentDto) {
        log.info("Пользователь id={} добавляет комментарий для вещи id={}", userId, itemId);
        commentDto.setAuthorId(userId);
        commentDto.setItemId(itemId);
        return commentService.addComment(commentDto);
    }
}
