package com.example.market.mapper;

import com.example.market.dto.responses.OrderItemResponse;
import com.example.market.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.name", target = "itemName")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "priceAtPurchase", target = "priceAtPurchase")
    OrderItemResponse toDto(OrderItem orderItem);

    // Eğer sadece itemId geliyor ve item objesi oluşmayacaksa "ignore"
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "priceAtPurchase", target = "priceAtPurchase")
    OrderItem toEntity(OrderItemResponse response);
}
