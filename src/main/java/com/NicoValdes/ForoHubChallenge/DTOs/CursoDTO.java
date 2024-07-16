package com.NicoValdes.ForoHubChallenge.DTOs;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CursoDTO(
        //Long id,
        @NotNull(message = "el nombre del curso no puede estar en blanco")
        String nombre,

        @NotNull(message = "la categoria no puede estar en blanco ")
        Integer categoriaId



) {}
