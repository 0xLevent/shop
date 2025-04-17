package com.example.market;

import com.example.market.dto.requests.CreateItemRequest;
import com.example.market.dto.requests.UpdateItemRequest;
import com.example.market.dto.responses.GetAllItemsResponse;
import com.example.market.dto.responses.GetByIdItemResponse;
import com.example.market.entity.Item;
import com.example.market.repository.ItemRepository;
import com.example.market.service.impl.ItemServiceImpl;
import com.example.market.util.mappers.ModelMapperService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ModelMapperService modelMapperService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    public void testGetById() {
        Item item = new Item();
        item.setId(1);
        item.setName("Test Item");

        GetByIdItemResponse response = new GetByIdItemResponse();
        response.setId(1);
        response.setName("Test Item");

        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(modelMapperService.forResponse()).thenReturn(modelMapper);
        when(modelMapper.map(item, GetByIdItemResponse.class)).thenReturn(response);

        GetByIdItemResponse result = itemService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Item", result.getName());
    }

    @Test
    public void testGetById_ItemNotFound() {
        when(itemRepository.findById(999)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.getById(999);
        });

        assertEquals("ID 999 ile item bulunamadı!", exception.getMessage());
    }

    @Test
    public void testGetAll() {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Item 1");

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Item 2");

        List<Item> items = Arrays.asList(item1, item2);

        GetAllItemsResponse response1 = new GetAllItemsResponse();
        response1.setId(1);
        response1.setName("Item 1");

        GetAllItemsResponse response2 = new GetAllItemsResponse();
        response2.setId(2);
        response2.setName("Item 2");

        when(itemRepository.findAll()).thenReturn(items);
        when(modelMapperService.forResponse()).thenReturn(modelMapper);
        when(modelMapper.map(item1, GetAllItemsResponse.class)).thenReturn(response1);
        when(modelMapper.map(item2, GetAllItemsResponse.class)).thenReturn(response2);

        List<GetAllItemsResponse> results = itemService.getAll();

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("Item 1", results.get(0).getName());
        assertEquals("Item 2", results.get(1).getName());
    }

    @Test
    public void testAdd() {
        // Arrange
        CreateItemRequest request = new CreateItemRequest();
        request.setName("New Item");

        Item item = new Item();
        item.setName("New Item");

        when(modelMapperService.forRequest()).thenReturn(modelMapper);
        when(modelMapper.map(request, Item.class)).thenReturn(item);
        when(itemRepository.existsByName("New Item")).thenReturn(false);

        itemService.add(request);

        verify(itemRepository).save(item);
    }

    @Test
    public void testAdd_ItemAlreadyExists() {
        CreateItemRequest request = new CreateItemRequest();
        request.setName("Existing Item");

        Item item = new Item();
        item.setName("Existing Item");

        when(modelMapperService.forRequest()).thenReturn(modelMapper);
        when(modelMapper.map(request, Item.class)).thenReturn(item);
        when(itemRepository.existsByName("Existing Item")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.add(request);
        });

        assertEquals("Bu isimde zaten bir item var: Existing Item", exception.getMessage());
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    public void testUpdate() {
        // Arrange
        UpdateItemRequest request = new UpdateItemRequest();
        request.setId(1);
        request.setName("Updated Item");

        Item existingItem = new Item();
        existingItem.setId(1);
        existingItem.setName("Original Item");

        when(itemRepository.findById(1)).thenReturn(Optional.of(existingItem));
        when(modelMapperService.forRequest()).thenReturn(modelMapper);

        itemService.update(request);

        verify(modelMapper).map(request, existingItem);
        verify(itemRepository).save(existingItem);
    }

    @Test
    public void testUpdate_ItemNotFound() {
        UpdateItemRequest request = new UpdateItemRequest();
        request.setId(999);
        request.setName("Updated Item");

        when(itemRepository.findById(999)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.update(request);
        });

        assertEquals("Güncellenecek item bulunamadı: 999", exception.getMessage());
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    public void testDelete() {
        int itemId = 1;
        when(itemRepository.existsById(itemId)).thenReturn(true);

        itemService.delete(itemId);

        verify(itemRepository).deleteById(itemId);
    }

    @Test
    public void testDelete_ItemNotFound() {
        int itemId = 999;
        when(itemRepository.existsById(itemId)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.delete(itemId);
        });

        assertEquals("Silinecek item bulunamadı: 999", exception.getMessage());
        verify(itemRepository, never()).deleteById(anyInt());
    }
}