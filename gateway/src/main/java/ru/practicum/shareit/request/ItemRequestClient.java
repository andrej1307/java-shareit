package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createRequest(Long customerId, ItemRequestDto itemRequestDto) {
        return post("", customerId, itemRequestDto);
    }

    public ResponseEntity<Object> findRequest(Long customerId, Long itemId) {
        return get("/" + itemId, customerId);
    }

    public ResponseEntity<Object> findRequestsByCustomerId(Long customerId) {
        return get("", customerId);
    }

    public ResponseEntity<Object> findAllRequests(Long customerId) {
        return get("/all", customerId);
    }
}
