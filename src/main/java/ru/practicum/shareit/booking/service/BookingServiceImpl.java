package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.enums.SearchState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.excepton.AccessDeniedException;
import ru.practicum.shareit.excepton.InternalServerException;
import ru.practicum.shareit.excepton.NotFoundException;
import ru.practicum.shareit.excepton.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepository userRepository,
                              ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public BookingDto addBooking(BookingDto bookingDto, Long userId) {
        if (bookingDto.getItemId() == null) {
            throw new InternalServerException("Отсутствуют сведения о необходимой вещи.");
        }
        if (userId == null) {
            throw new ValidationException("Отсутствуют сведения о заказчике.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("Не найден пользователь id=" + userId));

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() ->
                        new NotFoundException("Не найдена вещ id=" +
                                bookingDto.getItemId()));
        if (!item.getAvailable()) {
            throw new ValidationException(ItemMapper.toItemDto(item) + " Недоступна для бронирования.");
        }
        bookingDto.setBookerId(userId);
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        Booking savedBooking = bookingRepository.save(booking);
        Long bookingId = savedBooking.getId();
        savedBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new InternalServerException("Ошибка при добавлении запроса на бронирование."));
        return BookingMapper.toBookingDto(savedBooking);
    }

    @Override
    public BookingDto findBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new NotFoundException("Не найден запрос на бронирование id=" + bookingId));
        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Пользователь id=" + userId + " не является автором запроса или хозяином вещи.");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approvedBooking(Long id, Long editorId, Boolean approved) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Не найден запрос id=" + id));
        if (!booking.getItem().getOwner().getId().equals(editorId)) {
            throw new ValidationException("Пользователь id=" + editorId + " не является хозяином.");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(savedBooking);
    }

    @Override
    public List<BookingDto> findBookingsByOwner(Long ownerId, SearchState state) {
        userRepository.findById(ownerId)
                .orElseThrow(() ->
                        new NotFoundException("Не найден пользователь id=" + ownerId));
        List<Booking> bookings = new ArrayList<>();
        if (state.equals(SearchState.ALL)) {
            bookings = bookingRepository.findAllByItem_OwnerIdOrderByStartDesc(ownerId);
        } else if (state.equals(SearchState.REJECTED)) {
            bookings = bookingRepository.findBookingsByOwnerAndStatus(ownerId, BookingStatus.REJECTED);
        } else if (state.equals(SearchState.WAITING)) {
            bookings = bookingRepository.findBookingsByOwnerAndStatus(ownerId, BookingStatus.WAITING);
        } else if (state.equals(SearchState.PAST)) {
            bookings = bookingRepository.findBookingsByOwnerPast(ownerId, Instant.now());
        } else if (state.equals(SearchState.CURRENT)) {
            bookings = bookingRepository.findBookingsByOwnerCurrent(ownerId, Instant.now());
        } else if (state.equals(SearchState.FUTURE)) {
            bookings = bookingRepository.findBookingsByOwnerFuture(ownerId, Instant.now());
        }
        return bookings.stream().map(BookingMapper::toBookingDto).toList();
    }

    @Override
    public List<BookingDto> findBookingByBooker(Long bookerId, SearchState state) {
        userRepository.findById(bookerId)
                .orElseThrow(() ->
                        new NotFoundException("Не найден пользователь id=" + bookerId));

        List<Booking> bookings = new ArrayList<>();
        if (state.equals(SearchState.REJECTED)) {
            bookings = bookingRepository.findBookingsByBookerAndStatus(bookerId, BookingStatus.REJECTED);
        } else if (state.equals(SearchState.WAITING)) {
            bookings = bookingRepository.findBookingsByBookerAndStatus(bookerId, BookingStatus.WAITING);
        } else if (state.equals(SearchState.ALL)) {
            bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
        } else if (state.equals(SearchState.PAST)) {
            bookings = bookingRepository.findByBooker_IdAndEndIsBefore(bookerId, Instant.now(), Sort.by("DESC", "start"));
        } else if (state.equals(SearchState.CURRENT)) {
            bookings = bookingRepository.findBookingsByBookerIdCurrent(bookerId, Instant.now());
        } else if (state.equals(SearchState.FUTURE)) {
            bookings = bookingRepository.findByBooker_IdAndStartIsAfter(bookerId, Instant.now(), Sort.by("DESC", "start"));
        }
        return bookings.stream().map(BookingMapper::toBookingDto).toList();
    }
}
