package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;

/**
 * Клас обработки запросов на Вещи
 */
@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createItemRequest(
            @RequestHeader(UserMapper.HEADER_USER_ID) final Long customerId,
            @RequestBody final ItemRequestDto itemRequestDto) throws Exception {
        log.info("Пользователь id={} делает заказ : {}", customerId, itemRequestDto.getDescription());
        return itemRequestService.create(customerId, itemRequestDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RequestWithItemsDto findItemRequest(
            @RequestHeader(UserMapper.HEADER_USER_ID) final Long userId,
            @PathVariable final Long id) {
        log.info("Пользователь id={} просматривает заказ id={}", userId, id);
        return itemRequestService.findReqestsById(userId, id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestWithItemsDto> findRequestsByUserId(
            @RequestHeader(UserMapper.HEADER_USER_ID) final Long userId) {
        log.info("Пользователь id={} просматривает свои заказы.", userId);

        return itemRequestService.findReqestsByCustomerId(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestWithItemsDto> findAllRequests(
            @RequestHeader(UserMapper.HEADER_USER_ID) final Long userId) {
        log.info("Пользователь id={} просматривает все заказы.", userId);
        return itemRequestService.findAllReqests(userId);
    }
}
