package com.example.market.mapper;

import com.example.market.entity.User;
import com.example.market.dto.responses.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "balance", target = "balance")
    UserResponse toDto(User user);
}
