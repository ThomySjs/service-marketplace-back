package com.servicemarketplace.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.dto.user.UpdateUserDTO;
import com.servicemarketplace.api.dto.user.UserDTO;
import com.servicemarketplace.api.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Permite a un usuario ver los detalles de su cuenta.")
    /**
     * El usuario tiene que estar logueado para obtener los detalles de su cuenta
     */
    @GetMapping("/details")
    public ResponseEntity<UserDTO> getAccountDetails() {
        return ResponseEntity.ok(userService.getAccountDetails());
    }


    @Operation(summary = "Permite editar los datos de un usuario.")
    @PutMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser(@ModelAttribute UpdateUserDTO dto)
    {
        if (dto.profilePicture() != null ) {
            return ResponseEntity.ok(userService.updateProfilePicture(dto.profilePicture()));
        }

        userService.updateUser(dto);
        return ResponseEntity.ok(userService.updateUser(dto));

    }
}
