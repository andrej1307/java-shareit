package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    @Autowired
    private final UserService userService;

    @Test
    void createUser() {
        UserDto userDto = new UserDto(1L,
                "User Test create",
                "user@test.create");
        UserDto savedUserDto = userService.createUser(userDto);
        assertNotNull(savedUserDto,
                "Пользователь не соххраняется в репозитории");

        assertThat(savedUserDto.getId(), notNullValue());
        assertThat(savedUserDto.getEmail(), equalTo(userDto.getEmail()));
        assertThat(savedUserDto.getName(), equalTo(userDto.getName()));
    }

    @Test
    void getUserById() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getAllUsers() {
    }
}