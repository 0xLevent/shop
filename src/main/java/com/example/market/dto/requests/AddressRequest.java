package com.example.market.dto.requests;

import lombok.Data;

@Data
public class AddressRequest {
    private String addressLine;
    private String city;
    private String country;
    private String postalCode;
}