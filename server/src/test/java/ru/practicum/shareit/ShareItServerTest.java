package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
class ShareItServerTest {

    @Test
    void contextLoads() throws Exception {
    }

    @Test
    public void testMain() {
        ShareItServer.main(new String[]{});
    }
}