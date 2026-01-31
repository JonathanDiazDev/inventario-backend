package com.tuproyecto.inventory.dto;

import com.tuproyecto.inventory.model.TipoMovimiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovimientoDTO(@NotNull Long productoId,
                            @NotNull Integer cantidad,
                            @NotBlank TipoMovimiento tipo // "ENTRADA" o "SALIDA"
                            ) {
}
