package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.excepton.ValidationException;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Класс хранения в памяти информации о пользователях
 */
@Repository
public class UserInMemoryStorage implements UserStorage {
    private final Map<Long, User> users;
    private Long idMain = 1L;

    public UserInMemoryStorage() {
        users = new HashMap<>();
    }

    @Override
    public Optional<User> createUser(User user) {
        if (users.containsValue(user)) {
            throw new ValidationException("пользователь уже существует: "
                    + user.toString());
        }
        user.setId(idMain++);
        users.put(user.getId(), user);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        if (users.containsKey(id)) {
            return Optional.ofNullable(users.get(id));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> updateUser(User user) {
        users.put(user.getId(), user);
        return Optional.ofNullable(user);
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }
}
