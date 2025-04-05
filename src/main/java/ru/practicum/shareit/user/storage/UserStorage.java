package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Optional<User> createUser(User user);

    Optional<User> getUserById(Long id);

    Optional<User> updateUser(User user);

    void deleteUser(Long id);

    Collection<User> findAllUsers();

    void deleteAllUsers();
}
