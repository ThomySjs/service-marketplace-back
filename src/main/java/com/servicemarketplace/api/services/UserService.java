package com.servicemarketplace.api.services;

import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.dto.UserDTO;

public interface UserService {

    User getUserByEmail(String email);

    User getUserById(Long id);

    UserDTO getAccountDetails();
}
