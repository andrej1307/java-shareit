package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.excepton.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
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

    Long ownerId = 0L;
    Long itemId = 0L;
    ItemDto testItemDto;

    @BeforeEach
    void setUp() {
        if (ownerId == 0L) {
            // создаем пользователя - хозяина вещей
            UserDto userDto = new UserDto();
            userDto.setName("User1");
            userDto.setEmail("user1@items.test");
            UserDto savedUserDto = userService.createUser(userDto);
            assertThat(savedUserDto.getId(), notNullValue());
            ownerId = savedUserDto.getId();
        }
        itemService.deleteAllItems();
    }

    @Test
    void addItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item1");
        itemDto.setDescription("Item1");
        itemDto.setAvailable(true);
        itemDto.setRequestId(null);
        ItemDto savedItem = itemService.addItem(itemDto, ownerId);
        assertThat(savedItem.getId(), notNullValue());
        assertEquals(savedItem.getName(), itemDto.getName());
        assertEquals(savedItem.getDescription(), itemDto.getDescription());
        itemId = savedItem.getId();
        testItemDto = savedItem;
    }

    @Test
    void updateItem() throws Exception {
        if (itemId == 0L) {
            addItem();
        }
        ItemDto updItemDto = new ItemDto();
        updItemDto.setId(itemId);
        updItemDto.setDescription("Item1 uddated");

        ItemDto itemDto = itemService.updateItem(updItemDto, ownerId);
        assertThat(itemDto.getId(), notNullValue());
        assertEquals(itemDto.getDescription(), updItemDto.getDescription());
    }

    @Test
    void getItems() throws Exception {
        if (itemId == 0L) {
            addItem();
        }
        ItemCommentsDto icd = itemService.getItem(itemId, ownerId);
        assertThat(icd.getId(), notNullValue());
        assertEquals(testItemDto.getId(), icd.getId());
        assertEquals(testItemDto.getName(), icd.getName());
        assertEquals(testItemDto.getDescription(), icd.getDescription());
    }

    @Test
    void deleteItem() throws Exception {
        if (itemId == 0L) {
            addItem();
        }
        itemService.deleteItem(itemId, ownerId);
        assertThrows(NotFoundException.class,
                () -> {
                    itemService.getItem(itemId, ownerId);
                },
                "Чтение удаленной вещи должно приводить к исключению.");
    }

    /**
     * Тестируем чтение списка вещей пользователя
     */
    @Test
    void getItemsByOwnerId() {
        // заполняем список вещей
        List<ItemDto> sourceItems = makeItems(ownerId, 3);

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

    @Test
    void searchItems() throws Exception {
        // заполняем список вещей
        List<ItemDto> sourceItems = makeItems(ownerId, 3);

        List<ItemDto> targetItems = itemService.searchItemsByText("Item_2")
                .stream().toList();
        assertNotNull(targetItems);
        assertTrue(targetItems.size() > 0);
    }

    private List<ItemDto> makeItems(Long ownerId, Integer count) {
        List<ItemDto> sourceItems = new ArrayList<>();
        int maxItems = 3;
        for (int i = 1; i <= count; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setName("Item_" + i);
            itemDto.setDescription("Itemms test item_" + i);
            itemDto.setAvailable(true);
            sourceItems.add(itemDto);
            itemService.addItem(itemDto, ownerId);
        }
        return sourceItems;
    }

}