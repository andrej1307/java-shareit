package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.excepton.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService{
    private ItemRequestRepository itemRequestRepository;
    private UserRepository userRepository;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                  UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ItemRequestDto create(Long customerId, ItemRequestDto itemRequestDto) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь id=" + customerId));

        itemRequestDto.setCustomer(UserMapper.toUserDto(customer));
        itemRequestDto.setCreated(Instant.now());
        ItemRequest savedItemRequest = itemRequestRepository.save(
                ItemRequestMapper.ToItemRequest(itemRequestDto));
        return ItemRequestMapper.ToItemRequestDto(savedItemRequest);
    }

    @Override
    public ItemRequestDto findReqestsById(Long userId, Long id) {
        User customer = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь id=" + userId));
        ItemRequest request = itemRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найден запрос id=" + id));
        return ItemRequestMapper.ToItemRequestDto(request);
    }

    @Override
    public List<ItemRequestDto> findReqestsByCustomerId(Long CustomerId) {
        return List.of();
    }

    @Override
    public List<ItemRequestDto> findAllReqests(Long CustomerId) {
        return List.of();
    }
}
