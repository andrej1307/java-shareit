package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.excepton.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                  UserRepository userRepository,
                                  ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public ItemRequestDto create(Long customerId, ItemRequestDto itemRequestDto) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь id=" + customerId));

        itemRequestDto.setRequestor(UserMapper.toUserDto(customer));
        itemRequestDto.setCreated(Instant.now());
        ItemRequest savedItemRequest = itemRequestRepository.save(
                ItemRequestMapper.toItemRequest(itemRequestDto));
        return ItemRequestMapper.toItemRequestDto(savedItemRequest);
    }

    @Override
    public RequestWithItemsDto findReqestsById(Long userId, Long id) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь id=" + userId));
        ItemRequest request = itemRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найден запрос id=" + id));

        RequestWithItemsDto rwi = ItemRequestMapper.toRwiDto(request);
        List<ItemShortDto> items = itemRepository.findAllByRequest_IdEquals(id).stream()
                .map(ItemMapper::toItemShortDto)
                .toList();
        rwi.setItems(items);
        return rwi;
    }

    /**
     * Поиск собственных запросов заказчика
     *
     * @param customerId - идентификатор заказчика
     * @return - список запросов с ответными предложениями вещей
     */
    @Override
    public List<RequestWithItemsDto> findReqestsByCustomerId(Long customerId) {
        userRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь id=" + customerId));

        List<ItemRequest> reqests = itemRequestRepository.findAllByCustomer_IdEquals(
                customerId, Sort.by("created").descending());
        return addItemsToRequests(reqests);
    }

    @Override
    public List<RequestWithItemsDto> findAllReqests(Long customerId) {
        userRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь id=" + customerId));

        List<ItemRequest> reqests = itemRequestRepository
                .findAllNotCustomer_Id(customerId);
        return addItemsToRequests(reqests);
    }

    private List<RequestWithItemsDto> addItemsToRequests(List<ItemRequest> requests) {
        List<RequestWithItemsDto> rwiDtos = new ArrayList<>();
        Map<Long, RequestWithItemsDto> rwiDtoMap = new HashMap<>();
        for (int i = 0; i < requests.size(); i++) {
            ItemRequest itemRequest = requests.get(i);
            RequestWithItemsDto rwiDto = ItemRequestMapper.toRwiDto(itemRequest);
            rwiDtos.add(rwiDto);
            rwiDtoMap.put(itemRequest.getId(), rwiDto);
        }

        List<Long> ids = new ArrayList<>(rwiDtoMap.keySet());
        List<Item> items = itemRepository.findAllByRequest_IdIn(ids);
        for (Item item : items) {
            if (item.getRequest() != null) {
                long requestId = item.getRequest().getId();
                rwiDtoMap.get(requestId).getItems().add(ItemMapper.toItemShortDto(item));
            }
        }

        return rwiDtos;
    }
}
