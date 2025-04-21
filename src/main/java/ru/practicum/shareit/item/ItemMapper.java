package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestMapper;

import java.util.List;

public class ItemMapper {
    private ItemMapper() {
    }

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        if(item.getRequest() != null) {
            itemDto.setRequest(ItemRequestMapper.ToItemRequestDto(item.getRequest()));
        }
        itemDto.setAvailable(item.getAvailable());

        return itemDto;
    }

    public static ItemCommentsDto toItemCommentsDto(Item item) {
        ItemCommentsDto itemDto = new ItemCommentsDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        if(item.getRequest() != null) {
            itemDto.setRequest(ItemRequestMapper.ToItemRequestDto(item.getRequest()));
        }
        itemDto.setComments(List.of());
        return itemDto;

    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        if(itemDto.getRequest() != null) {
            item.setRequest(ItemRequestMapper.ToItemRequest(itemDto.getRequest()));
        }
        return item;
    }

}
