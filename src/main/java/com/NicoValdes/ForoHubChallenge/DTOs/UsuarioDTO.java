package com.NicoValdes.ForoHubChallenge.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
        Integer id,

        @NotBlank(message = "El nombre no puede estar en blanco ")
        String nombre,

        @NotBlank
        @Email(message = "Ingrese un formato correcto de email")
        String correoElectronico,

        @NotBlank(message = "La contraseña no puede estar en blanco")
        String contrasena
) {}

