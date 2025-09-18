package com.servicemarketplace.api.services.impl;

import java.util.List;
import java.util.Optional;

import com.servicemarketplace.api.domain.entities.Category;
import com.servicemarketplace.api.domain.entities.Service;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.ServiceRepository;
import com.servicemarketplace.api.dto.service.ServiceCreatedDTO;
import com.servicemarketplace.api.dto.service.ServiceDTO;
import com.servicemarketplace.api.dto.service.ServiceDetailsResponse;
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
    public List<ServiceListResponse> getByCategory(String[] categoryId) {
        return serviceRepository.findByCategory(categoryId);
    }

    @Override
    public List<ServiceListResponse> getByTitle(String title) {
        return serviceRepository.findByTitle(title);
    }

    @Override
    public List<ServiceListResponse> getAllNotDeleted() {
        return serviceRepository.findAllNotDeleted();
    }

    @Override
    public Service getByIdNotDeleted(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("La id del servicio no puede ser nula.");
        }
        Optional<Service> service = serviceRepository.findByIdAndDeletedFalse(id);
        if (service.isEmpty()) {
            throw new ResourceNotFoundException("El servicio no existe.");
        }

        return service.get();
    }

    /**
     * Verifica si el usuario NO es el creador del servicio y NO es admin.
     *
     * <ul>
     *   <li>Permite que los administradores puedan dar de baja un servicio.</li>
     *   <li>Evita que un usuario pueda borrar un servicio de otro usuario.</li>
     * </ul>
     *
     * @param service
     */
    @Override
    public void validateServiceOwner(Service service) {
        //Obtiene el usuario desde el contexto
        User user = userService.getUserFromContext();

        if (!service.getSeller().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new InvalidOperationException("No tienes permisos para realizar esta accion.");
        }
    }

    @Override
    public void deleteById(Long id) {
        //Obtiene el servicio
        Service service = getByIdNotDeleted(id);

        //Validar que el usuario es el creador del servicio
        validateServiceOwner(service);

        //Elimina el servicio (borrado logico)
        service.setDeleted(true);
        serviceRepository.save(service);

        //Elimina la imagen de cloudinary
        imageService.delete(service.getImagePath());
    }

    @Override
    public ServiceCreatedDTO update(ServiceDTO request) {
        //Obtiene el servicio
        Service service = getByIdNotDeleted(request.id());

        //Valida que el usuario es el creador del servicio
        validateServiceOwner(service);

        //Verifica si el usuario cambio la categoria del servicio
        if (!request.categoryId().equals(service.getCategory().getId())) {
            Category newCategory = categoryService.getCategoryById(request.categoryId());
            service.setCategory(newCategory);
        }

        //Verifica si el usuario subio una imagen nueva
        if (request.image() != null) {
            //Carga la imagen nueva
            String newImage = imageService.upload(request.image());

            //Si el servicio tiene una imagen previa, la borra
            if (service.getImagePath() != null) {
                imageService.delete(service.getImagePath());
            }

            service.setImagePath(newImage);
        }

        service.setTitle(request.title());
        service.setDescription(request.description());
        service.setPrice(request.price());

        return ServiceMapper.toServiceCreatedDTO(serviceRepository.save(service));
    }

    @Override
    public ServiceDetailsResponse getServiceDetails(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("La id no puede ser nula.");
        }

        Optional<ServiceDetailsResponse> details = serviceRepository.getServiceDetailsNotDeleted(id);
        if (details.isEmpty()) {
            throw new ResourceNotFoundException("El servicio no existe o fue eliminado.");
        }

        return details.get();
    }
}
