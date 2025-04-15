package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.enums.BookingStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, Instant end, Sort sort);

    List<Booking> findByBooker_IdAndStartIsAfter(Long bookerId, Instant start, Sort sort);

    List<Booking> findAllByItem_OwnerIdOrderByStartDesc(Long ownerId);

    @Query("SELECT bk FROM Booking AS bk " +
            "WHERE bk.booker.id = ?1 AND bk.status = ?2 " +
            "ORDER BY bk.start DESC")
    List<Booking> findBookingsByBookerAndStatus(Long bookerId, BookingStatus status);

    @Query("SELECT bk FROM Booking AS bk " +
            "WHERE bk.booker.id = ?1 AND (bk.start < ?2 AND bk.end > ?2) " +
            "ORDER BY bk.start DESC")
    List<Booking> findBookingsByBookerIdCurrent(Long bookerId, Instant instant);

    @Query("SELECT bk FROM Booking AS bk " +
            "WHERE bk.item.owner.id = ?1 AND bk.status = ?2 " +
            "ORDER BY bk.start DESC")
    List<Booking> findBookingsByOwnerAndStatus(Long ownerId, BookingStatus status);

    @Query("SELECT bk FROM Booking AS bk " +
            "WHERE bk.item.owner.id = ?1 AND bk.end < ?2 " +
            "ORDER BY bk.start DESC")
    List<Booking> findBookingsByOwnerPast(Long ownerId, Instant instant);

    @Query("SELECT bk FROM Booking AS bk " +
            "WHERE bk.item.owner.id = ?1 AND (bk.start < ?2 AND bk.end > ?2) " +
            "ORDER BY bk.start DESC")
    List<Booking> findBookingsByOwnerCurrent(Long ownerId, Instant instant);

    @Query("SELECT bk FROM Booking AS bk " +
            "WHERE bk.item.owner.id = ?1 AND bk.start > ?2 " +
            "ORDER BY bk.start DESC")
    List<Booking> findBookingsByOwnerFuture(Long ownerId, Instant instant);

    @Query("SELECT bk FROM Booking AS bk " +
            "WHERE (bk.booker.id = ?1 AND bk.item.id = ?2)" +
            "AND bk.status = \'APPROVED\'" +
            "ORDER BY bk.start DESC LIMIT 1")
    Booking findBookingsByBookerIdAndItemId(Long bookerId, Long itemId);

    @Query("SELECT bk FROM Booking AS bk " +
            "WHERE bk.item.id = ?1 AND bk.status = \'APPROVED\'" +
            "ORDER BY bk.start DESC LIMIT 1")
    Booking findLastBookingByItemId(Long itemId);
}
