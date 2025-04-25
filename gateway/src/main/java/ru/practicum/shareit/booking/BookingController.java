package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
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


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    /**
     * Ищем бронирования в соответствмм со статусом
     * @param userId - идентификатор пользователя
     * @param stateParam - статус бронирования
     * @param from
     * @param size
     * @return - результат поиска
     */
    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(HEADER_USER_ID) long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    /**
     * Бронирование вещии
     * @param userId - идентификатор пользователя
     * @param requestDto - данные о бронировании
     * @return - созданный объект бронирования
     */
    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader(HEADER_USER_ID) long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Создаем запрос на бронирование {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    /**
     * Редактирукм запрос на бронироапние
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public  ResponseEntity<Object> editBooking(
            @RequestHeader(HEADER_USER_ID) final Long editorId,
            @PathVariable Long id,
            @RequestParam Boolean approved) {
        log.info("Пользователь id={} редактирует запрос на бронирование id={}", editorId, id);
        return bookingClient.approvedBooking(id, editorId, approved);
    }

    /**
     * Ищем запрос на бронирование
     * @param userId - идентификатор пользователя
     * @param bookingId - идентификатор бронирования
     * @return - результат поиска
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(HEADER_USER_ID) long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }
}