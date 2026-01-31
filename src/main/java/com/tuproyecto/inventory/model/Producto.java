package com.tuproyecto.inventory.model;

import com.tuproyecto.inventory.dto.DatosRegistroProducto;
import jakarta.persistence.*;
import jakarta.validation.constraints.*; // Importante para las validaciones
import lombok.Data;

@Entity
@Table(name = "productos")
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(unique = true)
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy para que sea más eficiente
    @JoinColumn(name = "categoria_id") // Esta será la llave foránea en SQL
    private Categoria categoria;

    public Producto (){

    }

    public Producto(DatosRegistroProducto datos) {

        this.nombre = datos.nombre();
        this.precio = datos.precio();
        this.stock = datos.stock();

    }
}
