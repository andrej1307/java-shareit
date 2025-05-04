package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * Класс обработки http запросов о пользователях.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> findAllUser() {
        log.info("Запрашиваем список всех пользователей.");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto findUser(@PathVariable Long id) {
        log.info("Ищем пользователя id={}.", id);
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addNewUser(@RequestBody UserDto userDto) {
        log.info("Создаем пользователя : {}.", userDto.toString());
        return userService.createUser(userDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable Long id,
                              @RequestBody UserDto updUser) {
        updUser.setId(id);
        log.info("Обновляем данные о пользователе : {}", updUser);
        return userService.updateUser(updUser);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> deleteAllUsers() {
        log.info("Удаляем всех пользователей.");
        return userService.deleteAllUsers();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long id) {
        log.info("Удаляем пользователя id={}.", id);
        userService.deleteUser(id);
    }
}
