package com.servicemarketplace.api.services;

import java.util.List;

import com.servicemarketplace.api.dto.service.ServiceDTO;
import com.servicemarketplace.api.dto.service.ServiceListResponse;

public interface ServiceService {

    ServiceDTO create(ServiceDTO request);

    List<ServiceListResponse> getBySeller(Long id);

    List<ServiceListResponse> getByCategory(Long id);

    List<ServiceListResponse> getAllNotDeleted();
}
