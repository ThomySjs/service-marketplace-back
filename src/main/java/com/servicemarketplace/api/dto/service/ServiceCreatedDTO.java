package com.servicemarketplace.api.dto.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCreatedDTO {
    private Long id;
    private Long categoryId;
    private Long sellerId;
    private String image;
    private String title;
    private String description;
    private Double price;
}

