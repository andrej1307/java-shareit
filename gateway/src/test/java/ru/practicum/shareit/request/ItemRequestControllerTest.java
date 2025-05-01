package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ItemRequestClient itemRequestClient;

    @Autowired
    private MockMvc mvc;

    ItemRequestDto itemRequestDto = new ItemRequestDto(
            1L,
            "Request text.",
            new UserDto(1L, "user", "user@request.test"),
            Instant.now());

    @Test
    void createItemRequest() throws Exception {
        ResponseEntity<Object> response = new ResponseEntity<>(
                itemRequestDto, HttpStatus.CREATED);
        when(itemRequestClient.createRequest(anyLong(), any()))
                .thenReturn(response);

        mvc.perform(post("/requests")
                        .header(HEADER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
    }

    @Test
    void findItemRequest() throws Exception {
        ResponseEntity<Object> response = new ResponseEntity<>(
                itemRequestDto, HttpStatus.OK);
        when(itemRequestClient.findRequest(anyLong(), anyLong()))
                .thenReturn(response);

        mvc.perform(get("/requests/1")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
    }

    @Test
    void findRequestsByUserId() throws Exception  {
        ResponseEntity<Object> response = new ResponseEntity<>(
                itemRequestDto, HttpStatus.OK);
        when(itemRequestClient.findRequestsByCustomerId(anyLong()))
                .thenReturn(response);

        mvc.perform(get("/requests")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
    }

    @Test
    void findAllRequests() throws Exception  {
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        itemRequestDtos.add(itemRequestDto);

        ResponseEntity<Object> response = new ResponseEntity<>(
                itemRequestDtos, HttpStatus.OK);
        when(itemRequestClient.findAllRequests(anyLong()))
                .thenReturn(response);

        mvc.perform(get("/requests/all")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}