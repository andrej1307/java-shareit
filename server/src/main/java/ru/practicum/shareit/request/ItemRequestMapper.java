package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.user.UserMapper;

public class ItemRequestMapper {
    private ItemRequestMapper() {
    }

    public static ItemRequest ToItemRequest(ItemRequestDto requestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(requestDto.getId());
        itemRequest.setDescription(requestDto.getDescription());
        if (requestDto.getRequestor() != null) {
            itemRequest.setCustomer(UserMapper.toUser(requestDto.getRequestor()));
        }
        itemRequest.setCreated(requestDto.getCreated());
        return itemRequest;
    }

    public static ItemRequestDto ToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        if (itemRequest.getCustomer() != null) {
            itemRequestDto.setRequestor(UserMapper.toUserDto(itemRequest.getCustomer()));
        }
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }

    public static RequestWithItemsDto ToRwiDto(ItemRequest itemRequest) {
        RequestWithItemsDto itemRequestDto = new RequestWithItemsDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        if (itemRequest.getCustomer() != null) {
            itemRequestDto.setRequestor(UserMapper.toUserDto(itemRequest.getCustomer()));
        }
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }

}
