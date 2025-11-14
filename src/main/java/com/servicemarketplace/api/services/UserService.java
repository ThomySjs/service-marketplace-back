package com.servicemarketplace.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.dto.user.UpdateUserDTO;
import com.servicemarketplace.api.dto.user.UserDTO;
import com.servicemarketplace.api.dto.user.UserForAdmin;

import jakarta.validation.Valid;

public interface UserService {

    User getUserFromContext();

    User getUserByEmail(String email);

    User getUserById(Long id);

    UserDTO getAccountDetails();

    Page<UserForAdmin> getUserListForAdmin(Pageable pageable);

    UserDTO updateUser(@Valid UpdateUserDTO dto);

    String updateProfilePicture(MultipartFile profilePicture);

    String swtichUserRole(Long userId);
}
