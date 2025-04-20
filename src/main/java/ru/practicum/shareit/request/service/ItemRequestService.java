package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long customerId, ItemRequestDto itemRequestDto);

    ItemRequestDto findReqestsId(Long Id);

    List<ItemRequestDto> findReqestsByCustomerId(Long CustomerId);

    List<ItemRequestDto> findAllReqests(Long CustomerId);
}
