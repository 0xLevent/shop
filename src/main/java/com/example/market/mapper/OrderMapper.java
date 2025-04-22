package com.example.market.mapper;

import com.example.market.dto.responses.OrderResponse;
import com.example.market.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, AddressMapper.class})
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "shippingAddress", target = "shippingAddress") // Düzeltilen satır
    @Mapping(source = "items", target = "items")
    OrderResponse toDto(Order order);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "shippingAddress", target = "shippingAddress") // Düzeltilen satır
    @Mapping(source = "items", target = "items")
    Order toEntity(OrderResponse orderResponse);
}