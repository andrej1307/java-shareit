package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerIdEquals(Long ownerId);

    @Query("SELECT it FROM Item AS it " +
            "WHERE (UPPER(it.name) LIKE UPPER(?1) OR UPPER(it.description) LIKE UPPER(?1))" +
            "AND it.available = TRUE")
    List<Item> findByNameOrDescriptionContainingIgnoreCase(String textSearch);
}
