package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    private UserClient userClient;

    @Autowired
    private MockMvc mvc;

    UserDto testUserDto = new UserDto(
            1L,
            "tetUser",
            "test_user@gateway.test");

    @Test
    void addNewUser() throws Exception {
        ResponseEntity<Object> response = new ResponseEntity<>(
                testUserDto, HttpStatus.CREATED);

        when(userClient.addUser(any()))
                .thenReturn(response);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(testUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(testUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testUserDto.getName())))
                .andExpect(jsonPath("$.email", is(testUserDto.getEmail())));
    }

    @Test
    void findUser() throws Exception {
        ResponseEntity<Object> response = new ResponseEntity<>(
                testUserDto, HttpStatus.OK);

        when(userClient.findUserByID(anyLong()))
                .thenReturn(response);

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testUserDto.getName())))
                .andExpect(jsonPath("$.email", is(testUserDto.getEmail())));
    }

    @Test
    void findAllUser() throws Exception {
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(testUserDto);

        ResponseEntity<Object> response = new ResponseEntity<>(
                userDtos, HttpStatus.OK);

        when(userClient.findAllUsers())
                .thenReturn(response);

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

    }

    @Test
    void updateUser() throws Exception {
        ResponseEntity<Object> response = new ResponseEntity<>(
                testUserDto, HttpStatus.OK);

        when(userClient.updateUser(anyLong(), any()))
                .thenReturn(response);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(testUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testUserDto.getName())))
                .andExpect(jsonPath("$.email", is(testUserDto.getEmail())));
    }

}