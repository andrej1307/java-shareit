package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    private final ItemRequestService itemRequestService;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    @Test
    void addBooking() {
    }

    @Test
    void findBookingById() {
    }

    @Test
    void approvedBooking() {
    }

    @Test
    void findBookingsByOwner() {
    }

    @Test
    void findBookingByBooker() {
    }
}