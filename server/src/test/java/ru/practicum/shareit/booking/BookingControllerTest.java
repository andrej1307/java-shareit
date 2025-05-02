package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private BookingService bookingService;

    private BookingDto testBookingDto = new BookingDto(
            1L,
            LocalDateTime.of(2025, 04, 12, 10, 10),
            LocalDateTime.of(2025, 04, 15, 10, 10),
            1L,
            new ItemDto(1l, "Iteem", "Description", true, null, null, null),
            2L,
            new UserDto(2L, "User", "user@booking.controller.test"),
            BookingStatus.WAITING);

    @Autowired
    private MockMvc mvc;

    @Test
    void addBooking() throws Exception {
        when(bookingService.addBooking(any(), anyLong()))
                .thenReturn(testBookingDto);

        mvc.perform(post("/bookings")
                        .header(HEADER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(testBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(testBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.bookerId", is(testBookingDto.getBookerId()), Long.class));
    }

    @Test
    void editBooking() throws Exception {
        when(bookingService.approvedBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(testBookingDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.bookerId", is(testBookingDto.getBookerId()), Long.class));

    }

    @Test
    void findBooking() throws Exception {
        when(bookingService.findBookingById(anyLong(), anyLong()))
                .thenReturn(testBookingDto);

        mvc.perform(get("/bookings/1")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.bookerId", is(testBookingDto.getBookerId()), Long.class));

    }

    @Test
    void findBookingByBooker() throws Exception {
        List<BookingDto> bookingDtos = new ArrayList<>();
        bookingDtos.add(testBookingDto);

        when(bookingService.findBookingByBooker(anyLong(), any()))
                .thenReturn(bookingDtos);

        mvc.perform(get("/bookings?state=ALL")
                        .header(HEADER_USER_ID, 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findBookingByOwner() throws Exception {
        List<BookingDto> bookingDtos = new ArrayList<>();
        bookingDtos.add(testBookingDto);

        when(bookingService.findBookingsByOwner(anyLong(), any()))
                .thenReturn(bookingDtos);

        mvc.perform(get("/bookings/owner?state=ALL")
                        .header(HEADER_USER_ID, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}