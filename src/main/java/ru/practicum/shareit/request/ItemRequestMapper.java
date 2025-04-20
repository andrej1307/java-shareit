package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;

public class ItemRequestMapper {
    private ItemRequestMapper() {
    }

    public static ItemRequest ToItemRequest(ItemRequestDto requestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(requestDto.getId());
        itemRequest.setDescription(requestDto.getDescription());
        itemRequest.setCustomer(UserMapper.toUser(requestDto.getCustomer()));
        itemRequest.setCreated(requestDto.getCreated());
        return itemRequest;
    }

    public static ItemRequestDto ToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCustomer(UserMapper.toUserDto(itemRequest.getCustomer()));
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }
}
