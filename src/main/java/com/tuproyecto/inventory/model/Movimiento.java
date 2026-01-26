package com.tuproyecto.inventory.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;



@Entity
@Table(name = "movimientos")
@Data
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    private Integer cantidad;


    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

}
