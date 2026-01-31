package com.tuproyecto.inventory.dto;

import com.tuproyecto.inventory.model.Movimiento; // Ajusta a tu paquete real
import java.time.LocalDateTime;

public record MovimientoResponseDTO(
        Long id,
        String nombreProducto,
        Integer cantidad,
        LocalDateTime fecha
) {
    // Usamos el constructor para mapear la entidad de forma segura
    public MovimientoResponseDTO(Movimiento m) {
        this(
                m.getId(),
                m.getProducto().getNombre(),
                m.getCantidad(),
                m.getFecha()
        );
    }
}
