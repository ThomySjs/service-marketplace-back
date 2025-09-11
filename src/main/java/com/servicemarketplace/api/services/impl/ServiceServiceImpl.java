package com.servicemarketplace.api.services.impl;

import java.util.List;
import java.util.Optional;

import com.servicemarketplace.api.domain.entities.Category;
import com.servicemarketplace.api.domain.entities.Service;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.ServiceRepository;
import com.servicemarketplace.api.dto.service.ServiceCreatedDTO;
import com.servicemarketplace.api.dto.service.ServiceDTO;
import com.servicemarketplace.api.dto.service.ServiceListResponse;
import com.servicemarketplace.api.exceptions.auth.InvalidOperationException;
import com.servicemarketplace.api.exceptions.auth.ResourceNotFoundException;
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

    @Override
    public Service getByIdNotDeleted(Long id) {
        Optional<Service> service = serviceRepository.findByIdAndDeletedFalse(id);
        if (service.isEmpty()) {
            throw new ResourceNotFoundException("El servicio no existe.");
        }

        return service.get();
    }

    @Override
    public void deleteById(Long id) {
        //Obtiene el servicio
        Service service = getByIdNotDeleted(id);

        //Obtiene el usuario desde el contexto
        User user = userService.getUserFromContext();

        /*
            Verifica si el usuario NO es el creador del servicio y NO es admin
            - Permite que los administradores puedan dar de baja un servicio
            - Evita que un usuario pueda borrar un servicio de otro usuario
        */
        if (!service.getSeller().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new InvalidOperationException("No tienes permisos para realizar esta accion.");
        }

        //Elimina el servicio (borrado logico)
        service.setDeleted(true);
        serviceRepository.save(service);
    }
}
