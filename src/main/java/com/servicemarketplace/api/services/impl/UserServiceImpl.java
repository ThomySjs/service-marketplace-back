package com.servicemarketplace.api.services.impl;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.UserRepository;
import com.servicemarketplace.api.dto.UserDTO;
import com.servicemarketplace.api.exceptions.auth.UserNotFoundException;
import com.servicemarketplace.api.mappers.UserMapper;
import com.servicemarketplace.api.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO getAccountDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<User> foundUser = userRepository.findByEmail(email);
        if (foundUser.isEmpty()) {
            throw new UserNotFoundException("Usuario invalido.");
        }
        User user = foundUser.get();

        return UserMapper.toUserDTO(user);
    }
}
