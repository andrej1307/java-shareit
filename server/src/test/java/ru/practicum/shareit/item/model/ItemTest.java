package ru.practicum.shareit.item.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.User;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemTest {
    private final JacksonTester<Item> json;

    @Test
    void serialize() throws Exception {
        Item item = new Item(
                1L,
                "Item",
                "Item description",
                true,
                null
        );
        JsonContent<Item> result = json.write(item);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Item description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);

    }

}