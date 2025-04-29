package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserController;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
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
    private ItemService itemService;
    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mvc;

    @Test
    void findAllItems() throws Exception {
        List<ItemDto> sourceItems = makeItems(3);

        when(itemService.getItemsByOwnerId(anyLong()))
                .thenReturn(sourceItems);

        mvc.perform(get("/items")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(sourceItems.size()));
    }

    @Test
    public void onSearch() throws Exception {
        List<ItemDto> sourceItems = makeItems(2);

        when(itemService.searchItemsByText(anyString()))
                .thenReturn(sourceItems);

        mvc.perform(get("/items/search")
                        .header(HEADER_USER_ID, 1L)
                        .param("text", "test")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(sourceItems.size()));

    }

    @Test
    void findItemById() throws Exception {
        ItemCommentsDto itemDto = new ItemCommentsDto();
        itemDto.setId(1L);
        itemDto.setName("Item2");
        itemDto.setDescription("controller test find item by id");
        itemDto.setAvailable(true);

        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(get("/items/1")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    void createItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Name item3");
        itemDto.setDescription("controller test create new item");
        itemDto.setAvailable(true);

        when(itemService.addItem(any(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header(HEADER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

@Test
    void updateItem() throws Exception {
    ItemDto itemDto = new ItemDto();
    itemDto.setId(1L);
    itemDto.setName("Name item4");
    itemDto.setDescription("controller test update item");
    itemDto.setAvailable(true);

    when(itemService.updateItem(any(), anyLong()))
            .thenReturn(itemDto);
    mvc.perform(patch("/items/1")
                    .header(HEADER_USER_ID, 1L)
                    .content(mapper.writeValueAsString(itemDto))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
            .andExpect(jsonPath("$.name", is(itemDto.getName())))
            .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    void deleteItem() throws Exception {
        mvc.perform(delete("/items/1")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(itemService, Mockito.times(1))
                .deleteItem(1L, 1L);
    }

    @Test
    void deleteAllItems() throws Exception {
        mvc.perform(delete("/items")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(itemService, Mockito.times(1))
                .deleteAllItems();
    }

    @Test
    void addComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setItemId(1L);
        commentDto.setText("comment item");
        commentDto.setAuthorId(2L);
        commentDto.setCreated(Instant.now());

        when(commentService.addComment(any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .header(HEADER_USER_ID, 2L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    private List<ItemDto> makeItems(Integer count) {
        List<ItemDto> sourceItems = new ArrayList<>();
        int maxItems = 3;
        for (int i = 1; i <= count; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setName("Item_" + i);
            itemDto.setDescription("controller test item_" + i);
            itemDto.setAvailable(true);
            sourceItems.add(itemDto);
        }
        return sourceItems;
    }
}