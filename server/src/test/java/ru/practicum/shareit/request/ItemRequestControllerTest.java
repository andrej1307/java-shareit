package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
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
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mvc;

    @Test
    void createItemRequest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("description");
        itemRequestDto.setRequestor(new UserDto(1L, "user", "user@request.crate.test"));
        itemRequestDto.setCreated(Instant.now());

        when(itemRequestService.create(anyLong(), any()))
                .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .header(HEADER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));

    }

    @Test
    void findItemRequest() throws Exception {
        RequestWithItemsDto rwi = new RequestWithItemsDto();
        rwi.setId(1L);
        rwi.setDescription("description");
        rwi.setRequestor(new UserDto(1L, "user", "user@request.crate.test"));
        rwi.setCreated(Instant.now());
        when(itemRequestService.findReqestsById(anyLong(), anyLong()))
                .thenReturn(rwi);

        mvc.perform(get("/requests/1")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(rwi.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(rwi.getDescription())));

    }

    @Test
    void findRequestsByUserId() throws Exception {
        List<RequestWithItemsDto> rwiList = makeRequests(2);

        when(itemRequestService.findReqestsByCustomerId(anyLong()))
                .thenReturn(rwiList);

        mvc.perform(get("/requests")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findAllRequests() throws Exception {
        List<RequestWithItemsDto> rwiList = makeRequests(3);

        when(itemRequestService.findAllReqests(anyLong()))
                .thenReturn(rwiList);

        mvc.perform(get("/requests/all")
                        .header(HEADER_USER_ID, 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private List<RequestWithItemsDto> makeRequests(Integer count) {
        List<RequestWithItemsDto> rwiList = new ArrayList<>();
        int maxItems = 3;
        for (int i = 1; i <= count; i++) {
            RequestWithItemsDto rwi = new RequestWithItemsDto();
            rwi.setId((long) i);
            rwi.setDescription("controller test item_" + i);
            rwi.setRequestor(new UserDto(1L, "user", "user@request.crate.test"));
            rwi.setCreated(Instant.now().plusSeconds(60 * i));
            rwiList.add(rwi);
        }
        return rwiList;
    }
}