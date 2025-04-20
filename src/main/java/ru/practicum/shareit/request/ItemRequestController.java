package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.UserMapper;

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
            @RequestBody final ItemRequestDto itemRequestDto ) throws Exception {
        log.info("Пользователь id={} делает заказ : {}", customerId, itemRequestDto.getDescription());
        return itemRequestService.create(customerId, itemRequestDto);
    }
    
}
