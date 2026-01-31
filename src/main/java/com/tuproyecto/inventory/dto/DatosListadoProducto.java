package com.tuproyecto.inventory.dto;

import com.tuproyecto.inventory.model.Producto;

public record DatosListadoProducto(
        Long id,
        String nombre,
        Double precio,
        Integer stock,
        String categoria // <--- Aquí solo devolvemos el nombre, mucho más limpio
) {
    // Constructor para mapear desde la Entidad
    public DatosListadoProducto(Producto producto) {
        this(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                // Manejamos el caso de que sea null para no romper los productos viejos
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : "Sin Categoría"
        );
    }
}