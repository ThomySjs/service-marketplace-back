package com.servicemarketplace.api.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.servicemarketplace.api.domain.entities.ServiceRejectCause;
import com.servicemarketplace.api.dto.ServiceRejectCauseDTO;

public interface ServiceRejectCauseService {
    List<ServiceRejectCauseDTO> getAll();

    Page<ServiceRejectCause> getAllPaginated(Pageable pageable);

    void create(String message);

    void delete(Long causeId);
}

