package com.example.market.util.mappers;

import org.modelmapper.ModelMapper;


public interface ModelMapperService {

    ModelMapper forResponse();
    ModelMapper forRequest();
}
