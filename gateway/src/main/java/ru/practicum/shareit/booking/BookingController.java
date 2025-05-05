package ru.practicum.shareit.booking;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.validator.ValidAction;

/**
 * Обработка запроса на бронирование вещей
 */
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    /**
     * Ищем бронирования в соответствии со статусом
     *
     * @param userId     - идентификатор пользователя
     * @param stateParam - статус бронирования
     * @param from
     * @param size
     * @return - результат поиска
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookings(@RequestHeader(HEADER_USER_ID) long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный режим: " + stateParam));
        log.info("Ищем запросы state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    /**
     * Бронирование вещии
     *
     * @param userId     - идентификатор пользователя
     * @param requestDto - данные о бронировании
     * @return - созданный объект бронирования
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> bookItem(@RequestHeader(HEADER_USER_ID) long userId,
                                           @Validated(ValidAction.OnCreate.class)
                                           @RequestBody BookItemRequestDto requestDto) {
        log.info("Создаем запрос на бронирование {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    /**
     * Редактирукм запрос на бронироапние
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> editBooking(
            @RequestHeader(HEADER_USER_ID) final Long editorId,
            @PathVariable Long id,
            @RequestParam Boolean approved) {
        log.info("Пользователь id={} редактирует запрос на бронирование id={}, approved={}",
                editorId, id, approved);
        return bookingClient.approvedBooking(id, editorId, approved);
    }

    /**
     * Ищем запрос на бронирование
     *
     * @param userId    - идентификатор пользователя
     * @param bookingId - идентификатор бронирования
     * @return - результат поиска
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(HEADER_USER_ID) long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findBookingByOwner(
            @RequestHeader(HEADER_USER_ID) final Long ownerId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный режим: " + stateParam));
        log.info("Пользователь id={} просматривает запроcы на все свои вещи. state {}, userId={}, from={}, size={}",
                ownerId, state, from, size);
        return bookingClient.findBookingsByOwner(ownerId, state, from, size);
    }
}