package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class BookingMapper {
    private BookingMapper() {
    }

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setTimeStart(LocalDateTime.ofInstant(booking.getStart(), ZoneOffset.UTC));
        bookingDto.setTimeEnd(LocalDateTime.ofInstant(booking.getEnd(), ZoneOffset.UTC));
        bookingDto.setItemDto(ItemMapper.toItemDto(booking.getItem()));
        bookingDto.setUserDto(UserMapper.toUserDto(booking.getBooker()));
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getTimeStart().toInstant(ZoneOffset.UTC));
        booking.setEnd(bookingDto.getTimeEnd().toInstant(ZoneOffset.UTC));
        booking.setItem(ItemMapper.toItem(bookingDto.getItemDto()));
        booking.setBooker(UserMapper.toUser(bookingDto.getUserDto()));
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }
}
