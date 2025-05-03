package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.enums.SearchState;
import ru.practicum.shareit.excepton.NotFoundException;
import ru.practicum.shareit.excepton.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingServiceImpl bookingServiceImpl;

    Long ownerId = 0L;
    Long itemId = 0L;
    Long userId = 0L;
    Long bookingId = 0L;
    BookingDto sourceBookingDto;

    @Test
    void addBooking() throws Exception {
        User user = new User();
        user.setName("User1");
        user.setEmail("user1@booking.test");
        User owner = userRepository.save(user);
        assertThat(owner.getId(), notNullValue());
        ownerId = owner.getId();

        user = new User();
        user.setName("User2");
        user.setEmail("user2@booking.test");
        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId(), notNullValue());
        userId = savedUser.getId();

        Item item = new Item();
        item.setName("Item1");
        item.setDescription("Item1");
        item.setOwner(owner);
        item.setAvailable(true);
        item.setRequest(null);
        Item savedItem = itemRepository.save(item);
        assertThat(savedItem.getId(), notNullValue());
        itemId = savedItem.getId();

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setBookerId(userId);
        bookingDto.setStart(LocalDateTime.of(2025, 04, 12, 10, 10));
        bookingDto.setEnd(LocalDateTime.of(2025, 04, 15, 10, 10));
        bookingDto.setStatus(BookingStatus.WAITING);

        sourceBookingDto = bookingServiceImpl.addBooking(bookingDto, userId);
        assertThat(sourceBookingDto.getId(), notNullValue());
        bookingId = sourceBookingDto.getId();
    }

    @Test
    void findBookingById() throws Exception {
        if (bookingId.equals(0L)) {
            addBooking();
        }

        BookingDto bookingDto = bookingServiceImpl.findBookingById(
                bookingId, userId);
        assertThat(bookingDto, notNullValue());
        AssertionsForClassTypes.assertThat(bookingDto)
                .usingRecursiveComparison()
                .isEqualTo(sourceBookingDto);

        assertThrows(NotFoundException.class,
                () -> {
                    bookingServiceImpl.findBookingById(
                            1000L, userId);
                },
                "Чтение Несуществующего бронирования должно приводить к исключению.");
    }

    @Test
    void approvedBooking() throws Exception {
        if (bookingId.equals(0L)) {
            addBooking();
        }

        BookingDto bookingDto = bookingServiceImpl.approvedBooking(bookingId, ownerId, true);
        assertNotNull(bookingDto);
        assertEquals(bookingId, bookingDto.getId());

        assertThrows(ValidationException.class,
                () -> {
                    bookingServiceImpl.approvedBooking(bookingId, userId, true);
                },
                "Изменение статуса бронирования не хозяином должно приводить к исключению.");
    }

    @Test
    void findBookingsByOwner() throws Exception {
        if (bookingId.equals(0L)) {
            addBooking();
        }
        List<BookingDto> bookingDtoList = bookingServiceImpl.findBookingsByOwner(
                ownerId, SearchState.PAST);
        assertThat(bookingDtoList, notNullValue());
        assertTrue(bookingDtoList.size() > 0);

        bookingDtoList = bookingServiceImpl.findBookingsByOwner(
                ownerId, SearchState.CURRENT);
        assertThat(bookingDtoList, notNullValue());
        assertTrue(bookingDtoList.isEmpty());

        bookingDtoList = bookingServiceImpl.findBookingsByOwner(
                ownerId, SearchState.FUTURE);
        assertThat(bookingDtoList, notNullValue());
        assertTrue(bookingDtoList.isEmpty());
    }

    @Test
    void findBookingByBooker() throws Exception {
        if (bookingId.equals(0L)) {
            addBooking();
        }
        List<BookingDto> bookingDtoList = bookingServiceImpl.findBookingByBooker(
                userId, SearchState.PAST);
        assertThat(bookingDtoList, notNullValue());
        assertTrue(bookingDtoList.size() > 0);

        bookingDtoList = bookingServiceImpl.findBookingByBooker(
                userId, SearchState.CURRENT);
        assertThat(bookingDtoList, notNullValue());
        assertTrue(bookingDtoList.isEmpty());

        bookingDtoList = bookingServiceImpl.findBookingByBooker(
                userId, SearchState.FUTURE);
        assertThat(bookingDtoList, notNullValue());
        assertTrue(bookingDtoList.isEmpty());

    }
}