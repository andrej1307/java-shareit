package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.excepton.ConflictException;
import ru.practicum.shareit.excepton.InternalServerException;
import ru.practicum.shareit.excepton.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public UserDto createUser(UserDto newUserDto) throws ConflictException, InternalServerException {
        User user = userStorage.createUser(UserMapper.toUser(newUserDto))
                .orElseThrow(() -> new InternalServerException("Ошибка при создании пользователя."));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userStorage.getUserById(id)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден id=" + id));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto updUserDto) {
        Long id = updUserDto.getId();
        User user = userStorage.getUserById(id)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден id=" + id));
        if (updUserDto.getName() != null) {
            user.setName(updUserDto.getName());
        }
        if (updUserDto.getEmail() != null) {
            user.setEmail(updUserDto.getEmail());
        }
        user = userStorage.updateUser(user)
                .orElseThrow(() ->
                        new InternalServerException("Ошибка при обновлении пользователя"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.getUserById(id)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден id=" + id));
        userStorage.deleteUser(id);
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return userStorage.findAllUsers().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public Collection<UserDto> deleteAllUsers() {
        userStorage.deleteAllUsers();
        return getAllUsers();
    }
}
