package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.excepton.InternalServerException;
import ru.practicum.shareit.excepton.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {
    private final ItemRequestService itemRequestService;
    private final UserRepository userRepository;

    Long userId = 0L;
    Long requestId = 0L;

    @Test
    void create() {
        User newUser = new User(1L, "UserName", "user@request.test");
        User savedUser = userRepository.save(newUser);
        assertNotNull(savedUser,
                "Пользователь не соххраняется в репозитории.");
        userId = savedUser.getId();

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("request test description");
        itemRequestDto.setCreated(Instant.now());
        ItemRequestDto savedRequestDto = itemRequestService.create(
                userId,
                itemRequestDto);
        assertNotNull(savedRequestDto,
                "Запрос не соххраняется в репозитории.");
        requestId = savedRequestDto.getId();
    }


    @Test
    void findReqestsById() throws NotFoundException, InternalServerException, Exception {
        if (requestId.equals(0L)) {
            create();
        }
        RequestWithItemsDto rwi =
                itemRequestService.findReqestsById(userId, requestId);
        assertNotNull(rwi, "Запрос не найден.");
        assertEquals(requestId, rwi.getId(),
                "Идентификатор запроса не верен.");

        assertThrows(NotFoundException.class,
                () -> {
                    itemRequestService.findReqestsById(userId, 1000L);
                },
                "Чтение Несуществующего запроса должно приводить к исключению.");
    }

    @Test
    void findReqestsByCustomerId() {
        if (userId.equals(0L)) {
            create();
        }
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("request test by customer id");
        itemRequestDto.setCreated(Instant.now());
        ItemRequestDto savedRequestDto = itemRequestService.create(
                userId,
                itemRequestDto);
        List<RequestWithItemsDto> rwiList =
                itemRequestService.findReqestsByCustomerId(userId);
        assertThat(rwiList, notNullValue());
        assertTrue(rwiList.size() > 1);
    }

    @Test
    void findAllReqests() {
        if (userId.equals(0L)) {
            create();
        }
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("request test find all");
        itemRequestDto.setCreated(Instant.now());
        ItemRequestDto savedRequestDto = itemRequestService.create(
                userId,
                itemRequestDto);

        // для поиска "чужих" запросов заведем еще одного пользователя
        User newUser = new User(2L, "UserName2", "user2@request.test");
        User savedUser = userRepository.save(newUser);

        // ищем все "чужие запросы"
        List<RequestWithItemsDto> rwiList =
                itemRequestService.findAllReqests(savedUser.getId());
        assertThat(rwiList, notNullValue());
        assertTrue(rwiList.size() > 1);
    }
}