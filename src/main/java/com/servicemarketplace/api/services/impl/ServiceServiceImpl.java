package com.servicemarketplace.api.services.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.servicemarketplace.api.domain.entities.Category;
import com.servicemarketplace.api.domain.entities.Service;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.ServiceRepository;
import com.servicemarketplace.api.dto.service.ServiceCreatedDTO;
import com.servicemarketplace.api.dto.service.ServiceDTO;
import com.servicemarketplace.api.dto.service.ServiceListResponse;
import com.servicemarketplace.api.mappers.ServiceMapper;
import com.servicemarketplace.api.services.CategoryService;
import com.servicemarketplace.api.services.ImageService;
import com.servicemarketplace.api.services.ServiceService;
import com.servicemarketplace.api.services.UserService;

import lombok.RequiredArgsConstructor;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService{

    private final ServiceRepository serviceRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ImageService imageService;

    @Override
    public ServiceCreatedDTO create(ServiceDTO request){
        //Obtener el usuario
        User user = userService.getUserFromContext();

        //Obtener la categoria
        Category category = categoryService.getCategoryById(request.categoryId());

        //Carga la imagen y obtiene la ruta
        String image_path = imageService.upload(request.image());

        //Crear servicio
        Service service = new Service();
        service.setSeller(user);
        service.setCategory(category);
        service.setImagePath(image_path);
        service.setTitle(request.title());
        service.setDescription(request.description());

        if (request.price() != null && request.price() > 0 ) {
            service.setPrice(request.price());
        }

        return ServiceMapper.toServiceCreatedDTO(serviceRepository.save(service));
    }

    @Override
    public List<ServiceListResponse> getBySeller(Long id) {
        User seller = userService.getUserById(id);
        return serviceRepository.findBySeller(seller);
    }

    @Override
    public List<ServiceListResponse> getByCategory(Long id) {
        Category category = categoryService.getCategoryById(id);
        return serviceRepository.findByCategory(category);
    }

    @Override
    public List<ServiceListResponse> getAllNotDeleted() {
        return serviceRepository.findAllNotDeleted();
    }
}
