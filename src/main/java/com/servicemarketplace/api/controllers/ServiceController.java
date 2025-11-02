package com.servicemarketplace.api.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.servicemarketplace.api.dto.RejectServiceRequest;
import com.servicemarketplace.api.dto.service.ServiceCreatedDTO;
import com.servicemarketplace.api.dto.service.ServiceDTO;
import com.servicemarketplace.api.dto.service.ServiceDetailsResponse;
import com.servicemarketplace.api.dto.service.ServiceListResponse;
import com.servicemarketplace.api.services.ServiceService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping()
public class ServiceController
{

	private final ServiceService serviceService;

	@Operation(summary = "Crea un servicio.")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ServiceCreatedDTO> createService(@ModelAttribute ServiceDTO request)
	{
		return ResponseEntity.ok(serviceService.create(request));
	}

	@Operation(summary = "Obtiene los servicios, permite filtrar por vendedor o categoria.")
	@GetMapping("/services")
	public ResponseEntity<Page<ServiceListResponse>> getServices(
			@RequestParam(value = "seller", required = false) Optional<Long> sellerId,
			@RequestParam(value = "category", required = false) Optional<String[]> categoryId,
			@RequestParam(required = false) Optional<String> title, Pageable pageable)
	{

		if (title.isPresent() && categoryId.isPresent())
		{
			return ResponseEntity.ok(serviceService.getByCategoryAndTitle(categoryId.get(), title.get(), pageable));
		}

		if (sellerId.isPresent())
		{
			return ResponseEntity.ok(serviceService.getBySeller(sellerId.get(), pageable));
		}

		if (categoryId.isPresent())
		{
			return ResponseEntity.ok(serviceService.getByCategory(categoryId.get(), pageable));
		}

		if (title.isPresent())
		{
			return ResponseEntity.ok(serviceService.getByTitle(title.get(), pageable));
		}

		return ResponseEntity.ok(serviceService.getAllNotDeleted(pageable));
	}

	@Operation(summary = "Obtiene el detalle de un servicio.")
	@GetMapping("/services/{id}")
	public ResponseEntity<ServiceDetailsResponse> getServiceDetails(@PathVariable Long id)
	{
		return ResponseEntity.ok(serviceService.getServiceDetails(id));
	}

	@Operation(summary = "Borra un servicio.")
	@DeleteMapping("/services/{id}")
	public ResponseEntity<String> deleteService(@PathVariable Long id)
	{
		serviceService.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Actualiza un servicio.")
	@PutMapping(value = "/services", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ServiceCreatedDTO> updateService(@ModelAttribute ServiceDTO request)
	{
		return ResponseEntity.ok(serviceService.update(request));
	}


	@Operation(summary = "Obtiene todos los servicios pendientes por orden de creacion y estado pendiente")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin/services")
	public ResponseEntity<Page<ServiceListResponse>> getServicesForAdminView(Pageable pageable) {
		return ResponseEntity.ok(serviceService.getByStatusPendingForBO(pageable));
	}

	@Operation(summary = "Cambia el estado de un servicio a APPROVED")
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/admin/services/{id}/approve")
	public ResponseEntity<String> approveService(@PathVariable Long id) {
		serviceService.markAsApproved(id);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Cambia el estado de un servicio a REJECTED")
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/admin/services/{id}/reject")
	public ResponseEntity<String> rejectService(@PathVariable Long id, @Valid @RequestBody RejectServiceRequest body) {
		serviceService.markAsRejected(id, body.serviceRejectCauseId());
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Obtiene datos para metricas de dashboard")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin/services/metrics")
	public ResponseEntity<String> getMetrics() {
		return ResponseEntity.ok(serviceService.getMetrics());
	}

}
