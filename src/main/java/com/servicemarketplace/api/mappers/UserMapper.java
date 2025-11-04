package com.servicemarketplace.api.mappers;

import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.dto.service.ServiceListResponseWithStatus;
import com.servicemarketplace.api.dto.user.UserDTO;
import com.servicemarketplace.api.dto.user.UserForSubscriptionDTO;

public class UserMapper {

    /**
     * Mapea el usuario a un DTO con los datos no sensibles.
     */
    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .name(user.getName())
            .imagePath(user.getImagePath())
            .email(user.getEmail())
            .address(user.getAddress())
            .phone(user.getPhone())
            .createdAt(user.getCreatedAt())
            .createdServices(user.getServices().stream()
                .filter(s -> !s.isDeleted() && !s.isDisabled())
                .map(s -> new ServiceListResponseWithStatus(
                s.getId(),
                s.getCategory().getTitle(),
                s.getImagePath(),
                s.getTitle(),
                s.getPrice(),
                s.getDescription(),
                s.getStatus().getTranslation()
            ))
        .toList())
            .build();
    }

    public static UserForSubscriptionDTO toUserForSubscriptionDTO(User user) {
        return UserForSubscriptionDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .build();
    }
}
