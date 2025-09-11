package com.servicemarketplace.api.mappers;

import com.servicemarketplace.api.domain.entities.Service;
import com.servicemarketplace.api.dto.service.ServiceCreatedDTO;

public class ServiceMapper {

    /**
     * Mapea un servicio a un DTO
     * @param service
     * @return ServiceRequest object
     */
    public static ServiceCreatedDTO toServiceCreatedDTO(Service service) {
        return ServiceCreatedDTO.builder()
            .id(service.getId())
            .categoryId(service.getCategory().getId())
            .sellerId(service.getSeller().getId())
            .image(service.getImagePath())
            .title(service.getTitle())
            .description(service.getDescription())
            .price(service.getPrice())
            .build();
    }
}
