package com.example.market.mapper;


import com.example.market.dto.requests.AddressRequest;
import com.example.market.dto.responses.AddressResponse;
import com.example.market.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    Address toEntity(AddressRequest addressRequest);


    AddressResponse toDto(Address address);
    Address toEntity(AddressResponse addressResponse);
}
