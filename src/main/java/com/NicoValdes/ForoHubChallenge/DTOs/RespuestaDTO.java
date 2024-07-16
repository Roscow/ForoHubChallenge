package com.NicoValdes.ForoHubChallenge.DTOs;

import jakarta.validation.constraints.NotNull;

public record RespuestaDTO(
        @NotNull
        String mensaje,

        @NotNull
        Boolean solucion,

        @NotNull
        Integer topicoId
) {}
