package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemShortDtoTest {
    private final JacksonTester<ItemShortDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemShortDto itemShortDto = new ItemShortDto(
                1L,
                "Item",
                1L);
        JsonContent<ItemShortDto> result = json.write(itemShortDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Item");
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
    }
}