package com.example.market.dto.responses;

import lombok.Data;

@Data
public class AddressResponse {
    private Long id;
    private String addressLine;
    private String city;
    private String country;
    private String postalCode;
}