package com.servicemarketplace.api.services;

import org.springframework.web.multipart.MultipartFile;

import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.dto.user.UpdateUserDTO;
import com.servicemarketplace.api.dto.user.UserDTO;
import jakarta.validation.Valid;

public interface UserService {

    User getUserFromContext();

    User getUserByEmail(String email);

    User getUserById(Long id);

    UserDTO getAccountDetails();

    UserDTO updateUser(@Valid UpdateUserDTO dto);

    String updateProfilePicture(MultipartFile profilePicture);
}
