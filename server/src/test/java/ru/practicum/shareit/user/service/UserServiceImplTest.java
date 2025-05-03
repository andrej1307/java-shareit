package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.excepton.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование сервиса работы с пользователями
 */
@JdbcTest
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final UserService userService;

    @Test
    void createUser() {
        UserDto userDto = new UserDto(1L,
                "User Test create",
                "user@test.create");
        UserDto savedUserDto = userService.createUser(userDto);
        assertNotNull(savedUserDto,
                "Пользователь не соххраняется в репозитории :" + userDto.toString());

        assertThat(savedUserDto.getId(), notNullValue());
        assertThat(savedUserDto.getEmail(), equalTo(userDto.getEmail()));
        assertThat(savedUserDto.getName(), equalTo(userDto.getName()));
    }

    @Test
    void getUserById() {
        UserDto userDto = new UserDto(1L,
                "User Test getById",
                "user@get.by.id.test");
        UserDto savedUserDto = userService.createUser(userDto);
        assertNotNull(savedUserDto,
                "Пользователь не соххраняется в репозитории :" + userDto);

        Long userId = savedUserDto.getId();
        UserDto userDto2 = userService.getUserById(userId);

        assertThat(userDto2)
                .usingRecursiveComparison()
                .isEqualTo(savedUserDto);
    }

    @Test
    void updateUser() {
        UserDto userDto = new UserDto(1L,
                "User Test update",
                "user@update.test");
        UserDto savedUserDto = userService.createUser(userDto);
        assertNotNull(savedUserDto,
                "Пользователь не соххраняется в репозитории :" + userDto);

        savedUserDto.setEmail("update@test.update");
        UserDto updatedUserDto = userService.updateUser(savedUserDto);

        assertThat(updatedUserDto, notNullValue());
        assertThat(updatedUserDto)
                .usingRecursiveComparison()
                .isEqualTo(savedUserDto);

        Long userId = savedUserDto.getId();

    }

    @Test
    void deleteUser() {
        UserDto userDto = new UserDto(1L,
                "User Test update",
                "user@update.test");
        UserDto savedUserDto = userService.createUser(userDto);
        assertNotNull(savedUserDto,
                "Пользователь не соххраняется в репозитории :" + userDto);

        Long userId = savedUserDto.getId();
        userService.deleteUser(userId);

        assertThrows(NotFoundException.class,
                () -> {
                    userService.getUserById(userId);
                },
                "Чтение удаленного пользователя должно приводить к исключению.");
    }

    @Test
    void getAllUsers() {
        List<UserDto> sourceUsers = List.of(
                new UserDto(1L, "User1", "user1@get.all.test"),
                new UserDto(2L, "User2", "user2@get.all.test"),
                new UserDto(3L, "User3", "user3@get.all.test")
        );

        for (UserDto user : sourceUsers) {
            userService.createUser(user);
        }

        List<UserDto> targetUsers = userService.getAllUsers().stream().toList();

        assertThat(targetUsers, notNullValue());
        assertThat(targetUsers, hasSize(sourceUsers.size()));
        for (UserDto sourceUser : sourceUsers) {
            assertThat(targetUsers, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(sourceUser.getName())),
                    hasProperty("email", equalTo(sourceUser.getEmail()))
            )));
        }
    }

    @Test
    void deleteAllUsers() {
        getAllUsers();
        List<UserDto> targetUsers = userService.deleteAllUsers().stream().toList();
        assertThat(targetUsers, notNullValue());
        assertThat(targetUsers, hasSize(0));
    }
}