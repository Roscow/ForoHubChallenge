package com.NicoValdes.ForoHubChallenge.DTOs;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaDTO(
        Integer id,
        @NotBlank( message = "El nombre no puede estar vacio")
        String nombre
) {}
