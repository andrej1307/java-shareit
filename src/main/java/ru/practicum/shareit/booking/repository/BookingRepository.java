package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.enums.BookingStatus;

import java.time.Instant;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking>  findAllByBookerIdOrderByStartDesc(Long bookerId);

    @Query("SELECT bk FROM Booking AS bk " +
            "WHERE bk.booker.id = ?1 AND bk.status = ?2 " +
            "ORDER BY bk.start DESC")
    List<Booking> findBookingsByBookerAndStatus(Long bookerId, BookingStatus status);

    @Query("SELECT bk FROM Booking AS bk " +
            "WHERE (bk.booker.id = ?1 AND bk.end < ?2) " +
            "ORDER BY bk.start DESC")
    List<Booking> findBookingsByBookerIdPast(Long bookerId, Instant instant);

    @Query("SELECT bk FROM Booking AS bk " +
            "WHERE (bk.booker.id = ?1 AND bk.start > ?2) " +
            "ORDER BY bk.start DESC")
    List<Booking> findBookingsByBookerIdFuture(Long bookerId, Instant instant);

    @Query("SELECT bk FROM Booking AS bk " +
            "WHERE bk.booker.id = ?1 AND (bk.start < ?2 AND bk.end > ?2) " +
            "ORDER BY bk.start DESC")
    List<Booking> findBookingsByBookerIdCurrent(Long bookerId, Instant instant);

    List<Booking> findAllByItem_OwnerIdOrderByStartDesc(Long ownerId);

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
}
