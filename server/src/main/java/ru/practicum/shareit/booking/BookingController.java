package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.SearchState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validator.ValidAction;

import java.util.List;

/**
 * Обработка запрососов на бронирование вещей
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto addBooking(
            @RequestHeader(HEADER_USER_ID) final Long bookerId,
            @Validated(ValidAction.OnCreate.class)
            @RequestBody BookingDto bookingDto) {
        log.info("Пользователь id={} cоздает запрос на бронирование : {}", bookerId, bookingDto);
        return bookingService.addBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto editBooking(
            @RequestHeader(HEADER_USER_ID) final Long editorId,
            @PathVariable Long id,
            @RequestParam Boolean approved) {
        log.info("Пользователь id={} редактирует запрос на бронирование id={}", editorId, id);
        return bookingService.approvedBooking(id, editorId, approved);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto findBooking(
            @RequestHeader(HEADER_USER_ID) final Long userId,
            @PathVariable Long id) {
        log.info("Пользователь id={} просматривает запрос на бронирование id={}", userId, id);
        return bookingService.findBookingById(id, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> findBookingByBooker(
            @RequestHeader(HEADER_USER_ID) final Long bookerId,
            @RequestParam(defaultValue = "ALL") SearchState state) {
        log.info("Пользователь id={} просматривает свои запроcы на бронирование", bookerId);
        return bookingService.findBookingByBooker(bookerId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> findBookingByOwner(
            @RequestHeader(HEADER_USER_ID) final Long ownerId,
            @RequestParam(defaultValue = "ALL") SearchState state) {
        log.info("Пользователь id={} просматривает запроcы на вое вещи", ownerId);
        return bookingService.findBookingsByOwner(ownerId, state);
    }

}
