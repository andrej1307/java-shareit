package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.excepton.InternalServerException;
import ru.practicum.shareit.excepton.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Repository
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User createUser(User user) {
        return userStorage.createUser(user)
                .orElseThrow(() -> new InternalServerException("Ошибка при создании пользователя."));
    }

    @Override
    public User getUserById(Long id) {
        return userStorage.getUserById(id)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден id=" + id));
    }

    @Override
    public User updateUser(User updUser) {
        Long id = updUser.getId();
        User user = userStorage.getUserById(id)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден id=" + id));

        if(updUser.getName() != null) {
            user.setName(updUser.getName());
        }
        if(updUser.getEmail() != null) {
            user.setEmail(updUser.getEmail());
        }
        return userStorage.updateUser(user)
                .orElseThrow(() ->
                        new InternalServerException("Ошибка при обновлении пользователя"));
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.getUserById(id)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден id=" + id));
        userStorage.deleteUser(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userStorage.findAllUsers();
    }

    @Override
    public String deleteAllUsers() {
        userStorage.deleteAllUsers();
        return "Все пользователи удалены.";
    }
}
