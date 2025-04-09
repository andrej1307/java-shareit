package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class BookingMapper {
    private BookingMapper() {
    }

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(LocalDateTime.ofInstant(booking.getStart(), ZoneOffset.UTC));
        bookingDto.setEnd(LocalDateTime.ofInstant(booking.getEnd(), ZoneOffset.UTC));
        bookingDto.setItemId(booking.getItem().getId());
        bookingDto.setItem(booking.getItem());
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart().toInstant(ZoneOffset.UTC));
        booking.setEnd(bookingDto.getEnd().toInstant(ZoneOffset.UTC));
        Item item = new Item();
        item.setId(bookingDto.getItemId());
        booking.setItem(item);
        User booker = new User();
        booker.setId(bookingDto.getBookerId());
        booking.setBooker(booker);
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }
}
