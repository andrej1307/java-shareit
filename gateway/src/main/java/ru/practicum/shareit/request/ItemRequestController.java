package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestClient requestClient;
    private final ItemRequestClient itemRequestClient;

    /**
     * Создаем новый запрос
     *
     * @param customerId     - идентификатор заказчика
     * @param itemRequestDto - объект описания запроса
     * @return - сохраненный запрос
     * @throws Exception
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItemRequest(
            @RequestHeader(HEADER_USER_ID) final Long customerId,
            @RequestBody ItemRequestDto itemRequestDto) throws Exception {
        log.info("Пользователь id={} делает заказ : {}", customerId, itemRequestDto.getDescription());
        return itemRequestClient.createRequest(customerId, itemRequestDto);
    }

    /**
     * Поиск запроса по идентификатору
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findItemRequest(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable Long id) {
        log.info("Пользователь id={} просматривает заказ id={}", userId, id);
        return itemRequestClient.findRequest(userId, id);
    }

    /**
     * Поиск пользователем своих запросов
     */
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findRequestsByUserId(
            @RequestHeader(HEADER_USER_ID) Long userId) {
        log.info("Пользователь id={} просматривает свои заказы.", userId);
        return itemRequestClient.findRequestsByCustomerId(userId);
    }

    /**
     * Поиск пользователем всех чужих запросов
     */
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAllRequests(
            @RequestHeader(HEADER_USER_ID) final Long userId) {
        log.info("Пользователь id={} просматривает все заказы.", userId);
        return itemRequestClient.findAllRequests(userId);
    }
}
