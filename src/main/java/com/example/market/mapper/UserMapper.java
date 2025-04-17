package com.example.market.mapper;

import com.example.market.entity.User;
import com.example.market.dto.responses.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDto(User user);
}
