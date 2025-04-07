package ru.practicum.shareit.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validator.ValidAction;

import java.time.Instant;

/**
 * Класс описания запроса
 */
@Entity
@Table(name = "itemrequests", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255, message = "Максимальная длина описания - 255 символов.",
            groups = {ValidAction.OnCreate.class, ValidAction.OnUpdate.class})
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requestor;

    @Column(name = "created", nullable = false)
    private Instant created = Instant.now();
}
