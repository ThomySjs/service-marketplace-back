package com.servicemarketplace.api.services.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.servicemarketplace.api.config.Roles;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.UserRepository;
import com.servicemarketplace.api.dto.user.UpdateUserDTO;
import com.servicemarketplace.api.dto.user.UserDTO;
import com.servicemarketplace.api.dto.user.UserForAdmin;
import com.servicemarketplace.api.exceptions.auth.ResourceNotFoundException;
import com.servicemarketplace.api.mappers.UserMapper;
import com.servicemarketplace.api.services.ImageService;
import com.servicemarketplace.api.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    @Override
    public User getUserFromContext() {
        //Obtiene el mail del contexto
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return getUserByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> foundUser = userRepository.findByEmail(email);
        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("Usuario invalido.");
        }
        return foundUser.get();
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("Usuario invalido.");
        }
        return foundUser.get();
    }

    @Override
    public UserDTO getAccountDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = getUserByEmail(email);

        return UserMapper.toUserDTO(user);
    }

    @Override
    public Page<UserForAdmin> getUserListForAdmin(Pageable pageable) {
        return userRepository.getUserForAdmin(pageable);
    }

    @Override
    public UserDTO updateUser(@Valid UpdateUserDTO dto){
        //Obtiene el usuario del token
        User user = getUserFromContext();

        //Actualiza los datos
        user.fromDTO(dto);

        User updatedUser = userRepository.save(user);

        return UserMapper.toUserDTO(updatedUser);
    }

    @Override
    public String updateProfilePicture(MultipartFile profilePicture) {
        //Obtiene el usuario del token
        User user = getUserFromContext();

        //Sube la imagen y verifica que se haya subido
        String newImagePath = imageService.upload(profilePicture);
        if (newImagePath == null) {
            throw new IllegalStateException("Ocurrio un error al actualizar la imagen.");
        }

        //Elimina la imagen vieja
        if (user.getImagePath() != null) {
            imageService.delete(user.getImagePath());
        }

        user.setImagePath(newImagePath);
        userRepository.save(user);

        return newImagePath;
    }

    @Override
    public String swtichUserRole(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado.");
        }

        User foundUser = user.get();
        User contextUser = getUserFromContext();

        if (foundUser.getEmail() == contextUser.getEmail()) {
            throw new IllegalQueryOperationException("No puedes cambiar tu propio rol");
        }

        if (foundUser.isAdmin()) {
            foundUser.setRole(Roles.USER.name());
        } else {
            foundUser.setRole(Roles.ADMIN.name());
        }
        userRepository.save(foundUser);

        return "Se actualizo el rol del usuario a: " + (foundUser.getRole().equals(Roles.ADMIN.name()) ? "Administrador" : "Usuario");
    }
}
