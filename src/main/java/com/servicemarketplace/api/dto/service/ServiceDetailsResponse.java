package com.servicemarketplace.api.dto.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDetailsResponse {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private SellerDTO seller;
    private String categoryTitle;
}
