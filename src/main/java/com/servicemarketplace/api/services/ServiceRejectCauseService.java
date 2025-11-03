package com.servicemarketplace.api.services;

import java.util.List;

import com.servicemarketplace.api.dto.ServiceRejectCauseDTO;

public interface ServiceRejectCauseService {
    List<ServiceRejectCauseDTO> getAll();
}

