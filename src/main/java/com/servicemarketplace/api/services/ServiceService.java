package com.servicemarketplace.api.services;

import com.servicemarketplace.api.dto.ServiceDTO;

public interface ServiceService {

    ServiceDTO create(ServiceDTO request);
}
