package com.tuproyecto.inventory.dto;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarProducto(
        @NotNull Long id, // Necesitamos el ID para saber cuál buscar
        String nombre,
        Double precio,
        Integer stock,
        Long categoriaId // Opcional: si viene, actualizamos la categoría
) {}