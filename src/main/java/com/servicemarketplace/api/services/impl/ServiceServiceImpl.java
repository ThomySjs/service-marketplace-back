package com.servicemarketplace.api.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.servicemarketplace.api.domain.entities.ServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.servicemarketplace.api.domain.entities.Category;
import com.servicemarketplace.api.domain.entities.Service;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.ServiceRepository;
import com.servicemarketplace.api.domain.repositories.ServiceRejectCauseRepository;
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
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final ObjectMapper objectMapper;
    private final ServiceRejectCauseRepository serviceRejectCauseRepository;

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
    public Page<ServiceListResponse> getBySeller(Long id, Pageable pageable) {
        User seller = userService.getUserById(id);
        return serviceRepository.findBySeller(seller, pageable);
    }

    @Override
    public Page<ServiceListResponse> getByCategory(String[] categoryId, Pageable pageable) {
        return serviceRepository.findByCategory(categoryId, pageable);
    }

    @Override
    public Page<ServiceListResponse> getByTitle(String title, Pageable pageable) {
        return serviceRepository.findByTitle(title, pageable);
    }

    @Override
    public Page<ServiceListResponse> getByCategoryAndTitle(String[] category, String title, Pageable pageable) {
        return serviceRepository.findByCategoryAndTitleNotDeleted(category, title, pageable);
    }

    @Override
    public Page<ServiceListResponse> getAllNotDeleted(Pageable pageable) {
        return serviceRepository.findAllNotDeleted(pageable);
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

	@Override
	public Page<ServiceListResponse> getByStatusPendingForBO(Pageable pageable)
	{
		return serviceRepository.findByStatusPending(pageable);
	}

	@Override
	public void markAsApproved(Long id)
	{
		var serviceForApproval = serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontro el servicio con id " + id));
		serviceForApproval.setStatus(ServiceStatus.APPROVED);
		serviceRepository.save(serviceForApproval);
	}

	@Override
	@Transactional
	public void markAsRejected(Long id, Long serviceRejectCauseId)
	{
		var serviceForRejection = serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontro el servicio con id " + id));

        // Buscar la causa de rechazo
        var cause = serviceRejectCauseRepository.findById(serviceRejectCauseId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la causa de rechazo con id " + serviceRejectCauseId));

        // Marcar servicio como REJECTED
        serviceForRejection.setStatus(ServiceStatus.REJECTED);

        // Asociar la causa al servicio
        serviceForRejection.setServiceRejectCause(cause);

        // Guardar cambios en servicio
        serviceRepository.save(serviceForRejection);
	}

	@Override
	public String getMetrics()
	{
		return getCombinedServiceMetrics();
	}

	public String getCombinedServiceMetrics() {
		try {
			LocalDateTime fromDate = LocalDate.now().minusMonths(12).atStartOfDay();

			// --- 1️⃣ Primer dataset: por mes y estado (el que ya tenías) ---
			List<Object[]> monthlyResults = serviceRepository.findServiceStatsFromDate(fromDate);

			Map<String, Map<ServiceStatus, Long>> groupedByMonth = new LinkedHashMap<>();

			for (Object[] row : monthlyResults)
			{
				int year = ((Number) row[0]).intValue();
				int month = ((Number) row[1]).intValue();
				ServiceStatus status = (ServiceStatus) row[2];
				long count = ((Number) row[3]).longValue();

				String key = String.format("%02d-%d", month, year);
				groupedByMonth.putIfAbsent(key, new EnumMap<>(ServiceStatus.class));
				groupedByMonth.get(key).put(status, count);
			}

			ArrayNode monthlyArray = objectMapper.createArrayNode();
			groupedByMonth.forEach((key, counts) -> {
				String[] parts = key.split("-");
				int month = Integer.parseInt(parts[0]);
				int year = Integer.parseInt(parts[1]);

				String monthName = Month.of(month).getDisplayName(TextStyle.FULL, new Locale("es", "ES"));

				ObjectNode node = objectMapper.createObjectNode();
				node.put("label", capitalize(monthName) + " " + year);
				node.put("creados", counts.getOrDefault(ServiceStatus.APPROVED, 0L) + counts.getOrDefault(ServiceStatus.PENDING, 0L)
						+ counts.getOrDefault(ServiceStatus.REJECTED, 0L));
				node.put("aprobados", counts.getOrDefault(ServiceStatus.APPROVED, 0L));
				node.put("pendientes", counts.getOrDefault(ServiceStatus.PENDING, 0L));
				node.put("rechazados", counts.getOrDefault(ServiceStatus.REJECTED, 0L));
				monthlyArray.add(node);
			});

			// --- 2️⃣ Segundo dataset: agrupado solo por estado ---
			List<Object[]> statusResults = serviceRepository.findServiceCountByStatusFromDate(fromDate);

			ArrayNode statusArray = objectMapper.createArrayNode();
			for (Object[] row : statusResults)
			{
				ServiceStatus status = (ServiceStatus) row[0];
				long count = ((Number) row[1]).longValue();

				ObjectNode node = objectMapper.createObjectNode();
				switch (status)
				{
					case PENDING ->
					{
						node.put("label", "Pendientes");
						node.put("color", "#0088FE");
					}
					case APPROVED ->
					{
						node.put("label", "Aprobados");
						node.put("color", "#00C49F");
					}
					case REJECTED ->
					{
						node.put("label", "Rechazados");
						node.put("color", "#FF8042");
					}
				}
				node.put("value", count);
				statusArray.add(node);
			}

			// --- 3️⃣ Nuevo dataset: rechazados agrupados por causa ---
			List<Object[]> rejectedByCauseResults = serviceRepository.findRejectedCountByCauseFromDate(fromDate);
			ArrayNode rejectedByCauseArray = objectMapper.createArrayNode();
			for (Object[] row : rejectedByCauseResults) {
				Number idNum = (Number) row[0];
				Long causeId = idNum == null ? null : idNum.longValue();
				String message = (String) row[1];
				long count = ((Number) row[2]).longValue();

				ObjectNode node = objectMapper.createObjectNode();
				if (causeId != null) node.put("id", causeId);
				node.put("message", message);
				node.put("count", count);
				rejectedByCauseArray.add(node);
			}

			// --- 4️⃣ Combinar todos en un solo JSON ---
			ObjectNode root = objectMapper.createObjectNode();
			root.set("porMes", monthlyArray);
			root.set("porEstado", statusArray);
			root.set("rechazadosPorCausa", rejectedByCauseArray);

			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
		} catch (Exception e) {
			return null;
		}
	}

	private String capitalize(String s) {
		if (s == null || s.isEmpty()) return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
}
