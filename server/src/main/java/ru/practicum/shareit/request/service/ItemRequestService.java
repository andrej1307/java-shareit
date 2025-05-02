package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long customerId, ItemRequestDto itemRequestDto);

    RequestWithItemsDto findReqestsById(Long userId, Long id);

    List<RequestWithItemsDto> findReqestsByCustomerId(Long customerId);

    List<RequestWithItemsDto> findAllReqests(Long customerId);
}
