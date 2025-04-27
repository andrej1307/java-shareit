package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;

    /**
     * Тестируем чтение списка вещей пользователя
     */
    @Test
    void getItemsByOwnerId() {
        // создаем поельзователя - хозяина вещей
        UserDto userDto = new UserDto();
        userDto.setName("User1");
        userDto.setEmail("user1@get.items.test");
        UserDto savedUserDto = userService.createUser(userDto);
        assertThat(savedUserDto.getId(), notNullValue());
        Long ownerId = savedUserDto.getId();

        // заполняем список вещей
        List<ItemDto> sourceItems = new ArrayList<>();
        int maxItems = 3;
        for (int i = 1; i <= maxItems; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setName("Item_" + i);
            itemDto.setDescription("getItemms test item_" + i);
            itemDto.setAvailable(true);
            sourceItems.add(itemDto);
            itemService.addItem(itemDto, ownerId);
        }

        // Чидаем список вещей
        List<ItemDto> targetItems = itemService.getItemsByOwnerId(ownerId)
                .stream().toList();

        assertThat(targetItems, notNullValue());
        assertThat(targetItems, hasSize(sourceItems.size()));
        for (ItemDto sourceItem : sourceItems) {
            assertThat(targetItems, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(sourceItem.getName())),
                    hasProperty("description", equalTo(sourceItem.getDescription()))
            )));
        }
    }

}