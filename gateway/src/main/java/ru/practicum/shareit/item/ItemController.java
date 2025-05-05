package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validator.ValidAction;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItem(
            @RequestHeader(HEADER_USER_ID) final Long ownerId,
            @Validated(ValidAction.OnCreate.class) @RequestBody ItemDto itemDto) {
        log.info("Пользователь id={} добавляет вещь : {}", ownerId, itemDto.toString());
        return itemClient.createItem(ownerId, itemDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findItem(@PathVariable Long id,
                                           @RequestHeader(HEADER_USER_ID) Long userId) {
        log.info("Польдователь id={} просматривает информацию о вещи id={}.", userId, id);
        return itemClient.findItemById(id, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAllItems(
            @RequestHeader(HEADER_USER_ID) final Long ownerId) {
        log.info("Запрашиваем список вещей владельца id={}", ownerId);
        return itemClient.findAllItems(ownerId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> onSearch(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(name = "text") String text) {
        log.info("Ищем вещи содержащие текст \"{}\" в названии или описании.", text);
        return itemClient.searchItemsByText(userId, text);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateItem(
            @RequestHeader(HEADER_USER_ID) final Long ownerId,
            @PathVariable Long id,
            @RequestBody ItemDto itemDto) {
        log.info("Пользователь id={} обновляет сведения об элемете id={} {}", ownerId, id, itemDto);
        itemDto.setId(id);
        return itemClient.updateItem(id, ownerId, itemDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItem(
            @RequestHeader(HEADER_USER_ID) final Long ownerId,
            @PathVariable Long id) {
        log.info("пользователь id={}. Удаляет вещь id={}", ownerId, id);
        itemClient.deleteItem(id, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addComment(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable Long itemId,
            @RequestBody CommentDto commentDto) {
        log.info("Пользователь id={} добавляет комментарий для вещи id={}", userId, itemId);
        commentDto.setAuthorId(userId);
        commentDto.setItemId(itemId);
        return itemClient.addComment(itemId, userId, commentDto);
    }

}
