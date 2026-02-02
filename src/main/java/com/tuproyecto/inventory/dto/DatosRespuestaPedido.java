package com.tuproyecto.inventory.dto;

import java.time.LocalDateTime;

public record DatosRespuestaPedido(
        Long id,
        LocalDateTime fecha,
        String mesa,
        Double total
) {
}