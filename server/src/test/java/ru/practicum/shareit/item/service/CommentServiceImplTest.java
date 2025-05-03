package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentServiceImplTest {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentService commentService;

    @Test
    void addComment() {
        User user = new User();
        user.setName("User1");
        user.setEmail("user1@comment.test");
        User owner = userRepository.save(user);
        assertThat(owner.getId(), notNullValue());

        Item item = new Item();
        item.setName("Item1");
        item.setDescription("Item1");
        item.setOwner(owner);
        item.setAvailable(true);
        item.setRequest(null);
        Item savedItem = itemRepository.save(item);
        assertThat(savedItem.getId(), notNullValue());

        user = new User();
        user.setName("User2");
        user.setEmail("user2@comment.test");
        User booker = userRepository.save(user);
        assertThat(booker.getId(), notNullValue());

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart((LocalDateTime.of(2025, 04, 12, 10, 10)).toInstant(ZoneOffset.UTC));
        booking.setEnd((LocalDateTime.of(2025, 04, 15, 10, 10)).toInstant(ZoneOffset.UTC));
        booking.setStatus(BookingStatus.APPROVED);

        Booking savedBooking = bookingRepository.save(booking);
        assertThat(savedBooking.getId(), notNullValue());

        CommentDto commentDto = new CommentDto();
        commentDto.setItemId(item.getId());
        commentDto.setText("Text.");
        commentDto.setAuthorId(booker.getId());
        commentDto.setCreated(Instant.now());

        CommentDto commentDto1 = commentService.addComment(commentDto);
        assertThat(commentDto1.getId(), notNullValue());
        assertEquals(commentDto1.getText(), commentDto.getText());
        assertEquals(commentDto1.getItemId(), commentDto1.getItemId());
    }
}