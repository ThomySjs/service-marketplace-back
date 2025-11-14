package com.servicemarketplace.api.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.servicemarketplace.api.domain.entities.ServiceRejectCause;
import com.servicemarketplace.api.domain.repositories.ServiceRejectCauseRepository;
import com.servicemarketplace.api.dto.ServiceRejectCauseDTO;
import com.servicemarketplace.api.services.ServiceRejectCauseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceRejectCauseServiceImpl implements ServiceRejectCauseService {

    private final ServiceRejectCauseRepository repository;

    @Override
    public List<ServiceRejectCauseDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(c -> new ServiceRejectCauseDTO(c.getId(), c.getMessage()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ServiceRejectCause> getAllPaginated(Pageable pageable) {
        return repository.findAllPaginated(pageable);
    }

    @Override
    public void create(String message) {
        repository.save(ServiceRejectCause.builder()
            .message(message)
            .build()
        );
    }

    public void delete(Long causeId){
        repository.deleteById(causeId);
    }
}

