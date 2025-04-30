package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    private final ItemRequestService itemRequestService;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    Long ownerId = 0L;
    Long itemId = 0L;
    Long userId = 0L;
    Long bookingId = 0L;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingServiceImpl bookingServiceImpl;

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
        bookingDto.setStart(LocalDateTime.of(2025, 05, 12, 10, 10));
        bookingDto.setEnd(LocalDateTime.of(2025, 05, 15, 10, 10));
        bookingDto.setStatus(BookingStatus.WAITING);

        BookingDto savedBookingDto = bookingServiceImpl.addBooking(bookingDto, userId);
        assertThat(savedBookingDto.getId(), notNullValue());
        bookingId = savedBookingDto.getId();
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