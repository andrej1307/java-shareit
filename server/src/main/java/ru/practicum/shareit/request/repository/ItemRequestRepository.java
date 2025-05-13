package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByCustomer_IdEquals(Long customerId, Sort sort);

    @Query("SELECT ir FROM ItemRequest AS ir " +
            "WHERE ir.customer.id <> ?1 " +
            "ORDER BY ir.created DESC")
    List<ItemRequest> findAllNotCustomer_Id(Long customerId);
}
