package com.tuproyecto.inventory.service;

import com.tuproyecto.inventory.dto.DatosRegistroMovimiento;
import com.tuproyecto.inventory.model.Movimiento;
import com.tuproyecto.inventory.model.Producto;
import com.tuproyecto.inventory.model.TipoMovimiento;
import com.tuproyecto.inventory.repository.MovimientoRepository;
import com.tuproyecto.inventory.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MovimientoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    public Movimiento registrarMovimiento(DatosRegistroMovimiento datos) {
        Producto productoEncontrado = productoRepository.findById(datos.productoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (datos.tipoMovimiento() == TipoMovimiento.ENTRADA) {
            productoEncontrado.setStock(productoEncontrado.getStock() + datos.cantidad());
        }else{
            if (datos.cantidad() > productoEncontrado.getStock()) {
                throw new RuntimeException("Stock insuficiente");

            }
            productoEncontrado.setStock(productoEncontrado.getStock() - datos.cantidad());
        }
        productoRepository.save(productoEncontrado);

        Movimiento movimiento = new Movimiento();

        movimiento.setFecha(LocalDateTime.now());
        movimiento.setCantidad(datos.cantidad());
        movimiento.setTipo(datos.tipoMovimiento());
        movimiento.setProducto(productoEncontrado);

        return movimientoRepository.save(movimiento);


    }

}
