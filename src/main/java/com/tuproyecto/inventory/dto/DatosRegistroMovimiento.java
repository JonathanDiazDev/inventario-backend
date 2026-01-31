package com.tuproyecto.inventory.dto;

import com.tuproyecto.inventory.model.TipoMovimiento;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroMovimiento(

        @NotNull
        Long productoId,

        // ¿Qué pondrías aquí para la cantidad? (Quizás con una validación @Min)
        @NotNull
        @Min(value = 1)
        Integer cantidad,

        // ¿Y aquí para el tipo (ENTRADA/SALIDA)?
        @NotNull
        TipoMovimiento tipoMovimiento
        ) {

}