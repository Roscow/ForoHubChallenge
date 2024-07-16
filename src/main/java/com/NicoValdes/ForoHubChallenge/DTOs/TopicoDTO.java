package com.NicoValdes.ForoHubChallenge.DTOs;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


public record TopicoDTO(

        @NotBlank(message = "El título no puede estar en blanco")
        String titulo,

        @NotBlank(message = "El mensaje no puede estar en blanco")
        String mensaje,
        /*
        @NotNull(message = "La fecha de creación no puede estar en blanco")
        LocalDateTime fechaCreacion,
        */
        String status,

        @NotNull(message = "El ID del autor no puede estar en blanco")
        Integer autorId,

        @NotNull(message = "El ID del curso no puede estar en blanco")
        Integer cursoId
) {}