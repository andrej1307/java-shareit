package ru.practicum.shareit.booking;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validator.ValidAction;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Класс описания бронирования вещи
 */
@Entity
@Table(name = "bookings", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {ValidAction.OnUpdate.class}, message = "id должен быть определен")
    private Long id;

    @Column(name = "time_start")
    private Instant start;

    @Column(name = "time_end")
    private Instant end;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User booker;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
