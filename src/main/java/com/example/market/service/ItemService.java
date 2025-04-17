package com.example.market.service;

import com.example.market.dto.requests.CreateItemRequest;
import com.example.market.dto.requests.UpdateItemRequest;
import com.example.market.dto.responses.GetAllItemsResponse;
import com.example.market.dto.responses.GetByIdItemResponse;

import java.util.List;

public interface ItemService {

     GetByIdItemResponse getById(int id);
     List<GetAllItemsResponse> getAll();
     void add(CreateItemRequest createItemRequest);
     void update(UpdateItemRequest updateItemRequest);
     void delete(int id);

}
