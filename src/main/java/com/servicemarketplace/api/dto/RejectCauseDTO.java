package com.servicemarketplace.api.dto;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RejectCauseDTO {
    @Length(min = 10, message = "El motivo debe tener un mínimo de 10 caracteres")
    private String message;
}
