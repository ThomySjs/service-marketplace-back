package com.servicemarketplace.api.dto.service;

import com.servicemarketplace.api.dto.service.ServiceListResponse;

public class ServiceListResponseWithStatus extends ServiceListResponse {
    private String status;

    public ServiceListResponseWithStatus( Long id,
        String category,
        String image,
        String title,
        Double price,
        String description,
        String status)
    {
        super( id, category, image, title, price, description);
        this.status = status;
    }

    public ServiceListResponseWithStatus() {}

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
