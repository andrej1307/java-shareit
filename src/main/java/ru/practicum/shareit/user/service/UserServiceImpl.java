package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.excepton.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto newUserDto) {
        User user = userRepository.save(UserMapper.toUser(newUserDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден id=" + id));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto updUserDto) {
        Long id = updUserDto.getId();
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден id=" + id));
        if (updUserDto.getName() != null) {
            user.setName(updUserDto.getName());
        }
        if (updUserDto.getEmail() != null) {
            user.setEmail(updUserDto.getEmail());
        }
        User savedUser = userRepository.save(user);
        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException("Пользователь не найден id=" + id);
        }
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public Collection<UserDto> deleteAllUsers() {
        userRepository.deleteAll();
        return getAllUsers();
    }
}
