package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validator.ValidAction;

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

    /**
     * Метод поиска всех пользователей
     *
     * @return - список пользователей
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> findAllUser() {
        log.info("Запрашиваем список всех пользователей.");
        return userService.getAllUsers();
    }

    /**
     * Метод поиска пользователя по идентификатору
     *
     * @param id - идентификатор
     * @return - найденный объект
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto findUser(@PathVariable Long id) {
        log.info("Ищем пользователя id={}.", id);
        User user = userService.getUserById(id);
        return UserMapper.toUserDto(user);
    }

    /**
     * Метод добавления нового пользователя.
     *
     * @param userDto - объект для добавления
     * @return - подтверждение добавленного объекта
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addNewUser(@Validated(ValidAction.OnCreate.class) @RequestBody UserDto userDto) {
        log.info("Создаем пользователя : {}.", userDto.toString());
        User user = userService.createUser(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    /**
     * Метод обновления информации о пользователе.
     *
     * @param updUser - объект с обновленной информацией о пользователе
     * @return - подтверждение обновленного объекта
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser( @PathVariable Long id,
            @Validated(ValidAction.OnUpdate.class) @RequestBody UserDto updUser) {
        log.info("Обновляем данные о пользователе : {}", updUser.toString());
        User user = userService.updateUser(UserMapper.toUser(updUser));
        return UserMapper.toUserDto(user);
    }

    /**
     * Удаление всех пользователей
     *
     * @return - сообщение о выполнении
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public String deleteAllUsers() {
        log.info("Удаляем всех пользователей.");
        return userService.deleteAllUsers();
    }

    /**
     * Удаление пользователя по заданному идентификатору
     * @param id - идентификатор пользователя
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long id) {
        log.info("Удаляем пользователя id={}.", id);
        userService.deleteUser(id);
    }

}
