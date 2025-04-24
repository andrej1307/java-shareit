package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validator.ValidAction;

/**
 * Класс обработки http запросов о пользователях.
 */
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    /**
     * Метод добавления нового пользователя.
     *
     * @param userDto - объект для добавления
     * @return - подтверждение добавленного объекта
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addNewUser(@Validated(ValidAction.OnCreate.class) @RequestBody UserDto userDto) {
        log.info("Создаем пользователя : {}.", userDto.toString());
        return userClient.addUser(userDto);
    }

    /**
     * Метод поиска пользователя по идентификатору
     *
     * @param id - идентификатор
     * @return - найденный объект
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findUser(@PathVariable Long id) {
        log.info("Ищем пользователя id={}.", id);
        return userClient.findUserByID(id);
    }

    /**
     * Метод поиска всех пользователей
     *
     * @return - список пользователей
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAllUser() {
        log.info("Запрашиваем список всех пользователей.");
        return userClient.findAllUsers();
    }

    /**
     * Метод обновления информации о пользователе.
     *
     * @param updUserDto - объект с обновленной информацией о пользователе
     * @return - подтверждение обновленного объекта
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateUser(@PathVariable Long id,
                                             @Validated(ValidAction.OnUpdate.class) @RequestBody UserDto updUserDto) {
        updUserDto.setId(id);
        log.info("Обновляем данные о пользователе : {}", updUserDto);
        return userClient.updateUser(id, updUserDto);
    }

    /**
     * Удаление пользователя по заданному идентификатору
     *
     * @param id - идентификатор пользователя
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long id) {
        log.info("Удаляем пользователя id={}.", id);
        userClient.deleteUser(id);
    }
}

