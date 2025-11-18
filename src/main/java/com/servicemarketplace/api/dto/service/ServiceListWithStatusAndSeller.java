package com.servicemarketplace.api.dto.service;

import com.servicemarketplace.api.domain.entities.ServiceStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceListWithStatusAndSeller {
    private Long id;
    private String category;
    private String image;
    private String title;
    private Double price;
    private String description;
    private String seller;
    private ServiceStatus status;
}
