package com.tuproyecto.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroProducto(@NotBlank(message = "El nombre es obligatorio")
                                    String nombre,

                                    @NotNull(message = "El precio es obligatorio")
                                    @Min(value = 0, message = "El precio no puede ser negativo")
                                    Double precio,

                                    @NotNull(message = "El stock es obligatorio")
                                    @Min(value = 0, message = "El stock no puede ser negativo")
                                    Integer stock,

                                    @NotNull(message = "El ID de la categoría es obligatorio")
                                    Long categoriaId // <--- EL NOMBRE IMPORTA: Debe llamarse 'categoriaId'
) {
}
