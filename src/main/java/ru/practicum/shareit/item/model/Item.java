package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validator.ValidAction;

/**
 * Клас описания вещи для совместного использования
 */
@Entity
@Table(name = "items", schema = "public")
@Data
@EqualsAndHashCode(of = {"name", "description"})
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {ValidAction.OnUpdate.class}, message = "id должен быть определен")
    private Long id;

    @NotBlank(message = "Имя не может быть пустым", groups = {ValidAction.OnCreate.class})
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255, message = "Максимальная длина описания - 255 символов.",
            groups = {ValidAction.OnCreate.class, ValidAction.OnUpdate.class})
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "available")
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    public Item(Long id, String name, String description, Boolean available, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}
