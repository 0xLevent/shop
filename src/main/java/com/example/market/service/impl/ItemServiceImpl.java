package com.example.market.service.impl;

import com.example.market.dto.requests.CreateItemRequest;
import com.example.market.dto.requests.UpdateItemRequest;
import com.example.market.dto.responses.GetAllItemsResponse;
import com.example.market.dto.responses.GetByIdItemResponse;
import com.example.market.entity.Item;
import com.example.market.repository.ItemRepository;
import com.example.market.service.ItemService;
import com.example.market.util.mappers.ItemMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Getter
@Setter
@Data
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public GetByIdItemResponse getById(int id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID " + id + " ile item bulunamadı!"));

        return itemMapper.itemToGetByIdItemResponse(item);
    }

    @Override
    public List<GetAllItemsResponse> getAll() {
        List<Item> items = itemRepository.findAll();
        return itemMapper.itemListToGetAllItemsResponseList(items);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(CreateItemRequest createItemRequest) {
        Item item = itemMapper.createItemRequestToItem(createItemRequest);

        if (itemRepository.existsByName(item.getName())) {
            throw new IllegalArgumentException("Bu isimde zaten bir item var: " + item.getName());
        }
        itemRepository.save(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UpdateItemRequest updateItemRequest) {
        Item existingItem = itemRepository.findById(updateItemRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("Güncellenecek item bulunamadı: " + updateItemRequest.getId()));

        itemMapper.updateItemFromRequest(updateItemRequest, existingItem);
        itemRepository.save(existingItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(int id) {
        if (!itemRepository.existsById(id)) {
            throw new IllegalArgumentException("Silinecek item bulunamadı: " + id);
        }
        itemRepository.deleteById(id);
    }
}