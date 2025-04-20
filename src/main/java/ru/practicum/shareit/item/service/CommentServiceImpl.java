package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.excepton.NotFoundException;
import ru.practicum.shareit.excepton.ValidationException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              BookingRepository bookingRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public CommentDto addComment(CommentDto commentDto) {
        Long userId = commentDto.getAuthorId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден id=" + userId));

        Long itemId = commentDto.getItemId();
        Booking booking = bookingRepository.findBookingsByBookerIdAndItemId(userId, itemId);
        if (booking == null) {
            throw new NotFoundException("Пользователь id=" + userId +
                    " не бронировал вещь id=" + itemId);
        }

        LocalDateTime now = LocalDateTime.now();
        if (LocalDateTime.ofInstant(booking.getEnd(), ZoneOffset.UTC).isAfter(now)) {
            throw new ValidationException("Комментарий возможен после завершения срока аренды. " +
                    "Конец аренды : " + LocalDateTime.ofInstant(booking.getEnd(), ZoneOffset.UTC) +
                    " Текущее время : " + now);
        }

        Item item = booking.getItem();
        if (item.getOwner().getId().equals(userId)) {
            throw new ValidationException("Хозяину нельзя оставлять комментарий.");
        }

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setText(commentDto.getText());
        comment.setCreated(Instant.now());

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toDto(savedComment);
    }

}
