package com.example.market.util.mappers;

import com.example.market.dto.requests.CreateItemRequest;
import com.example.market.dto.requests.UpdateItemRequest;
import com.example.market.dto.responses.GetAllItemsResponse;
import com.example.market.dto.responses.GetByIdItemResponse;
import com.example.market.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(target = "id", ignore = true)
    Item createItemRequestToItem(CreateItemRequest createItemRequest);

    void updateItemFromRequest(UpdateItemRequest updateItemRequest, @MappingTarget Item item);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    GetByIdItemResponse itemToGetByIdItemResponse(Item item);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    GetAllItemsResponse itemToGetAllItemsResponse(Item item);

    List<GetAllItemsResponse> itemListToGetAllItemsResponseList(List<Item> items);
}