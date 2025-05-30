package ru.practicum.shareit.booking.dto;

public enum BookingStatus {
    WAITING,    //  новое бронирование
    APPROVED,   // бронирование подтверждено владельцем
    REJECTED,   // бронирование отклонено владельцем
    CANCELED    // бронирование отменено создателем
}
