package com.servicemarketplace.api.mappers;

import com.servicemarketplace.api.domain.entities.Service;
import com.servicemarketplace.api.dto.ServiceDTO;

public class ServiceMapper {

    /**
     * Mapea un servicio a un DTO
     * @param service
     * @return ServiceRequest object
     */
    public static ServiceDTO toServiceDTO(Service service) {
        return ServiceDTO.builder()
            .Id(service.getId())
            .categoryId(service.getCategory().getId())
            .sellerId(service.getSeller().getId())
            .title(service.getTitle())
            .description(service.getDescription())
            .price(service.getPrice())
            .build();
    }
}
