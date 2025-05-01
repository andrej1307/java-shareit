package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.ItemRequestController;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = ItemRequestController.class)
class BookingControllerTest {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ItemRequestClient itemRequestClient;

    @Autowired
    private MockMvc mvc;

    @Test
    void getBookings() {
    }

    @Test
    void bookItem() {
    }

    @Test
    void editBooking() {
    }

    @Test
    void getBooking() {
    }

    @Test
    void findBookingByOwner() {
    }
}