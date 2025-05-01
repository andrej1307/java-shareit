package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ItemClient itemClient;

    @Autowired
    private MockMvc mvc;

    ItemDto testItemDto = new ItemDto(
            1L,
            "Item1",
            "Item1",
            true,
            null,
            null,
            null);

    @Test
    void createItem() throws Exception{
        ResponseEntity<Object> response = new ResponseEntity<>(
                testItemDto, HttpStatus.CREATED);
        when(itemClient.createItem(anyLong(), any()))
                .thenReturn(response);

        mvc.perform(post("/items")
                        .header(HEADER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(testItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(testItemDto.getName())));


    }

    @Test
    void findItem() throws Exception{
        ResponseEntity<Object> response = new ResponseEntity<>(
                testItemDto, HttpStatus.OK);
        when(itemClient.findItemById(anyLong(), anyLong()))
                .thenReturn(response);

        mvc.perform(get("/items/1")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testItemDto.getName())));
    }

    @Test
    void findAllItems() throws Exception{
        List<ItemDto> itemDtos = new ArrayList<>();
        itemDtos.add(testItemDto);
        ResponseEntity<Object> response = new ResponseEntity<>(
                itemDtos, HttpStatus.OK);
        when(itemClient.findAllItems(anyLong()))
                .thenReturn(response);

        mvc.perform(get("/items")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void onSearch() throws Exception{
        List<ItemDto> itemDtos = new ArrayList<>();
        itemDtos.add(testItemDto);
        ResponseEntity<Object> response = new ResponseEntity<>(
                itemDtos, HttpStatus.OK);
        when(itemClient.searchItemsByText(anyLong(), anyString()))
                .thenReturn(response);

        mvc.perform(get("/items/search?text=Item")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void updateItem() throws Exception{
        ResponseEntity<Object> response = new ResponseEntity<>(
                testItemDto, HttpStatus.OK);
        when(itemClient.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(response);

        mvc.perform(patch("/items/1")
                        .header(HEADER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(testItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testItemDto.getName())));
    }

    @Test
    void addComment() throws Exception{
        CommentDto commentDto = new CommentDto();
        commentDto.setItemId(testItemDto.getId());
        commentDto.setText("Comment text");

        ResponseEntity<Object> response = new ResponseEntity<>(
                testItemDto, HttpStatus.CREATED);
        when(itemClient.addComment(anyLong(), anyLong(), any()))
                .thenReturn(response);

        mvc.perform(post("/items/1/comment")
                        .header(HEADER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}