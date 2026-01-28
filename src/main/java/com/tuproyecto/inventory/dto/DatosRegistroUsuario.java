package com.tuproyecto.inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DatosRegistroUsuario(

        @NotBlank @Email String email,
        @NotBlank String password) {
}
