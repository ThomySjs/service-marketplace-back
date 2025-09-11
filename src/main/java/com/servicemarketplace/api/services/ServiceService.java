package com.servicemarketplace.api.services;

import java.util.List;

import com.servicemarketplace.api.domain.entities.Service;
import com.servicemarketplace.api.dto.service.ServiceCreatedDTO;
import com.servicemarketplace.api.dto.service.ServiceDTO;
import com.servicemarketplace.api.dto.service.ServiceListResponse;

public interface ServiceService {

    Service getByIdNotDeleted(Long id);

    ServiceCreatedDTO create(ServiceDTO request);

    List<ServiceListResponse> getBySeller(Long id);

    List<ServiceListResponse> getByCategory(Long id);

    List<ServiceListResponse> getAllNotDeleted();

    void deleteById(Long id);
}
