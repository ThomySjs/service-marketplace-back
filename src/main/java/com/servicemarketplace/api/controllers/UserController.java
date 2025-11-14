package com.servicemarketplace.api.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.dto.user.UpdateUserDTO;
import com.servicemarketplace.api.dto.user.UserDTO;
import com.servicemarketplace.api.dto.user.UserForAdmin;
import com.servicemarketplace.api.dto.user.UserRoleDTO;
import com.servicemarketplace.api.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Permite a un usuario ver los detalles de su cuenta.")
    /**
     * El usuario tiene que estar logueado para obtener los detalles de su cuenta
     */
    @GetMapping("/users/details")
    public ResponseEntity<UserDTO> getAccountDetails() {
        return ResponseEntity.ok(userService.getAccountDetails());
    }


    @Operation(summary = "Permite editar los datos de un usuario.")
    @PutMapping(path = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser(@ModelAttribute UpdateUserDTO dto)
    {
        if (dto.profilePicture() != null ) {
            return ResponseEntity.ok(userService.updateProfilePicture(dto.profilePicture()));
        }

        userService.updateUser(dto);
        return ResponseEntity.ok(userService.updateUser(dto));

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Permite obtener un listado de usuarios.")
    @GetMapping(path = "/admin/users")
    public ResponseEntity<Page<UserForAdmin>> getUsers(Pageable pageable)
    {
        return ResponseEntity.ok(userService.getUserListForAdmin(pageable));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Permite cambiar el rol de un usuario")
    @PutMapping("/admin/users")
    public ResponseEntity<String> putMethodName(@RequestBody UserRoleDTO dto) {
        return ResponseEntity.ok(userService.swtichUserRole(dto.userId()));
    }
}
