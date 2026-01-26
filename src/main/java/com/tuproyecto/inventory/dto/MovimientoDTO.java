package com.tuproyecto.inventory.dto;

import com.tuproyecto.inventory.model.TipoMovimiento;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class MovimientoDTO {
    @NotNull (message = "El tipo de movimiento es obligatorio (Entrada o Salida)")
    private TipoMovimiento tipo;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

}
