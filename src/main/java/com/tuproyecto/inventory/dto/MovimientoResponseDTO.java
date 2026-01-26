package com.tuproyecto.inventory.dto;
import com.tuproyecto.inventory.model.TipoMovimiento;
import lombok.Data;
import java.time.LocalDateTime;


@Data
public class MovimientoResponseDTO {
    private Long  id;
    private LocalDateTime fecha;
    private Integer cantidad;
    private String nombreProducto;

    private TipoMovimiento tipo;

}