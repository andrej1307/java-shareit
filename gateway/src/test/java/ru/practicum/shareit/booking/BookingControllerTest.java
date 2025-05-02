package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private MockMvc mvc;

    private BookItemRequestDto bookItemRequestDto = new BookItemRequestDto(
            1L,
            LocalDateTime.of(2025, 04, 12, 10, 10),
            LocalDateTime.of(2025, 04, 15, 10, 10)
    );

    private BookingDto testBookingDto = new BookingDto(
            1L,
            LocalDateTime.of(2025, 04, 12, 10, 10),
            LocalDateTime.of(2025, 04, 15, 10, 10),
            1L,
            new ItemDto(1l, "Iteem", "Description", true, null, null, null),
            2L,
            new UserDto(2L, "User", "user@booking.controller.test"),
            BookingStatus.WAITING);

    @Test
    void getBookings() throws Exception {
        List<BookingDto> bookingDtos = List.of(testBookingDto);
        ResponseEntity<Object> response = new ResponseEntity<>(
                bookingDtos, HttpStatus.OK);
        when(bookingClient.getBookings(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(response);

        mvc.perform(get("/bookings?state=ALL&from=0&size=10")
                        .header(HEADER_USER_ID, 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void bookItem() throws Exception {
        ResponseEntity<Object> response = new ResponseEntity<>(
                testBookingDto, HttpStatus.CREATED);
        when(bookingClient.bookItem(anyLong(), any()))
                .thenReturn(response);
        mvc.perform(post("/bookings")
                        .header(HEADER_USER_ID, 2L)
                        .content(mapper.writeValueAsString(bookItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemId", is((int) bookItemRequestDto.getItemId())));
    }

    @Test
    void editBooking() throws Exception {
        testBookingDto.setStatus(BookingStatus.APPROVED);
        ResponseEntity<Object> response = new ResponseEntity<>(
                testBookingDto, HttpStatus.OK);
        when(bookingClient.approvedBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(response);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    void getBooking() throws Exception {
        ResponseEntity<Object> response = new ResponseEntity<>(
                testBookingDto, HttpStatus.OK);
        when(bookingClient.getBooking(anyLong(), anyLong()))
                .thenReturn(response);

        mvc.perform(get("/bookings/1")
                        .header(HEADER_USER_ID, 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findBookingByOwner() throws Exception {
        List<BookingDto> bookingDtos = List.of(testBookingDto);
        ResponseEntity<Object> response = new ResponseEntity<>(
                bookingDtos, HttpStatus.OK);
        when(bookingClient.findBookingsByOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(response);

        mvc.perform(get("/bookings/owner?state=ALL&from=0&size=10")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}