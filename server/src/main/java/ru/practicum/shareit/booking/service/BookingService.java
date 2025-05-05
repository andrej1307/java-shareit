package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.SearchState;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingDto bookingDto, Long userId);

    BookingDto approvedBooking(Long id, Long editorId, Boolean approved);

    BookingDto findBookingById(Long bookingId, Long userId);

    List<BookingDto> findBookingsByOwner(Long ownerId, SearchState state);

    List<BookingDto> findBookingByBooker(Long bookerId, SearchState state);
}
