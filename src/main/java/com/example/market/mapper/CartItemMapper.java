package com.example.market.mapper;

import com.example.market.dto.responses.CartItemResponse;
import com.example.market.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.name", target = "itemName")
    @Mapping(target = "totalPrice", expression = "java(cartItem.getQuantity() * cartItem.getItem().getPrice())")
    CartItemResponse toResponse(CartItem cartItem);

    List<CartItemResponse> toResponseList(List<CartItem> cartItems);
}