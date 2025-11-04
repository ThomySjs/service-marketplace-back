package com.servicemarketplace.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.servicemarketplace.api.domain.entities.Service;
import com.servicemarketplace.api.dto.service.ServiceCreatedDTO;
import com.servicemarketplace.api.dto.service.ServiceDTO;
import com.servicemarketplace.api.dto.service.ServiceDetailsResponse;
import com.servicemarketplace.api.dto.service.ServiceListResponse;

public interface ServiceService {

    Service getByIdNotDeleted(Long id);

    ServiceCreatedDTO create(ServiceDTO request);

    Page<ServiceListResponse> getBySeller(Long id, Pageable pageable);

    Page<ServiceListResponse> getByCategory(String[] id, Pageable pageable);

    Page<ServiceListResponse> getByTitle(String title, Pageable pageable);

    Page<ServiceListResponse> getByCategoryAndTitle(String[] category, String ttle, Pageable pageable);

    Page<ServiceListResponse> getAllNotDeleted(Pageable pageable);

    public ServiceDetailsResponse getServiceDetailsForAdmin(Long id);

    void validateServiceOwner(Service service);

    void deleteById(Long id);

    ServiceCreatedDTO update(ServiceDTO request);

    ServiceDetailsResponse getServiceDetails(Long id);

	 Page<ServiceListResponse> getByStatusPendingForBO(Pageable pageable);

	 void markAsApproved(Long id);

	 void markAsRejected(Long id, Long serviceRejectCauseId);

	 String getMetrics();

}
