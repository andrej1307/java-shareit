package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.excepton.ConflictException;
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
    private final Map<String, User> userEmails;
    private Long idMain = 1L;

    public UserInMemoryStorage() {
        users = new HashMap<>();
        userEmails = new HashMap<>();
    }

    @Override
    public Optional<User> createUser(User user) {
        if (userEmails.get(user.getEmail()) != null) {
            throw new ConflictException("пользователь уже существует: "
                    + user.getEmail());
        }
        user.setId(idMain++);
        users.put(user.getId(), user);
        userEmails.put(user.getEmail(), user);
        return Optional.of(user);
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
    public Optional<User> updateUser(User updUser) {
        User userExists = userEmails.get(updUser.getEmail());
        if (userExists != null) {
            if (!userExists.getId().equals(updUser.getId())) {
                throw new ConflictException("Обнаружен конфликт Email : " + userExists);
            }
        }
        userEmails.remove(users.get(updUser.getId()).getEmail());
        users.put(updUser.getId(), updUser);
        userEmails.put(updUser.getEmail(), updUser);
        return Optional.of(updUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = users.get(id);
        userEmails.remove(user.getEmail());
        users.remove(id);
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public void deleteAllUsers() {
        userEmails.clear();
        users.clear();
    }
}
