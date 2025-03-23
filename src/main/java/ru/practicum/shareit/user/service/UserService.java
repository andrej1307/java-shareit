package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserService {
    User createUser(User user);

    User getUserById(Long id);

    User updateUser(User user);

    void deleteUser(Long id);

    Collection<User> getAllUsers();

    String deleteAllUsers();
}
