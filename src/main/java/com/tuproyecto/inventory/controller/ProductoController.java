package com.tuproyecto.inventory.controller;

import com.tuproyecto.inventory.dto.MovimientoDTO;
import com.tuproyecto.inventory.dto.MovimientoResponseDTO;
import com.tuproyecto.inventory.model.Producto;
import com.tuproyecto.inventory.repository.MovimientoRepository;
import com.tuproyecto.inventory.repository.ProductoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tuproyecto.inventory.model.Movimiento;
import com.tuproyecto.inventory.model.TipoMovimiento;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController // Le dice a Spring que esto es una API
@RequestMapping("/api/productos") // La dirección web para entrar aquí
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoRepository movimientoRepository; // <--- Agrega esta línea

    // Este método nos devuelve todos los productos
            @GetMapping
            public List<Producto> obtenerProductos() {
                return productoRepository.findAll();
            }
            @GetMapping("/{id}")
            public Producto obtenerPorId(@PathVariable Long id) {
                return productoRepository.findById(id).orElse(null);
            }

    // Este método sirve para guardar un producto nuevo
    @PostMapping
    public Producto guardarProducto(@Valid @RequestBody Producto producto) {
        return productoRepository.save(producto);
    }
    // Método para borrar un producto por su ID
    @DeleteMapping("/{id}")
    public void borrar(@PathVariable Long id) {
        productoRepository.deleteById(id);
    }
    // Método para actualizar un producto
    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id,@Valid @RequestBody Producto productoNuevo) {
        // 1. Buscamos el producto que ya existe por su ID
        return productoRepository.findById(id).map(productoExistente -> {
            // 2. Le cambiamos los datos viejos por los nuevos
            productoExistente.setNombre(productoNuevo.getNombre());
            productoExistente.setPrecio(productoNuevo.getPrecio());
            productoExistente.setStock(productoNuevo.getStock());
            // 3. Guardamos los cambios
            return productoRepository.save(productoExistente);
        }).orElse(null); // Si no existe el ID, no hace nada (devuelve null)
    }
    @GetMapping("/buscar")
    public List<Producto> buscarPorNombre(@RequestParam String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @PostMapping("/{id}/movimientos")
    public ResponseEntity<?> registrarMovimiento(@PathVariable Long id, @Valid @RequestBody MovimientoDTO movimientoDTO) {
                Producto producto = productoRepository.findById(id).orElse(null);
                if(producto == null){
                    return ResponseEntity.notFound().build();
                }

                if(movimientoDTO.getTipo() == TipoMovimiento.SALIDA){
                    //validadr si hay suficiente stock para vendder
                    if (producto.getStock() < movimientoDTO.getCantidad()){
                        return ResponseEntity.badRequest().body("No hay suficiente stock. Stock actual: " + producto.getStock() );
                    }
                    //restar stock
                    producto.setStock(producto.getStock() - movimientoDTO.getCantidad());
                }else {
                    //Si es entrada, sumar stock
                    producto.setStock(producto.getStock() + movimientoDTO.getCantidad());
                }

                //guardar el cambio en el producto (actualizamos el stock)
                productoRepository.save(producto);

                //crear y guardar el registro en el historial
                Movimiento movimiento = new Movimiento();
                movimiento.setFecha(LocalDateTime.now());
                movimiento.setCantidad(movimientoDTO.getCantidad());
                movimiento.setTipo(movimientoDTO.getTipo());
                movimiento.setProducto(producto);

                movimientoRepository.save(movimiento);
                return ResponseEntity.ok(producto);
    }
    @GetMapping("/{id}/movimientos")
    public List<MovimientoResponseDTO> obtenerHistorial(@PathVariable Long id) {
                return movimientoRepository.findByProductoIdOrderByFechaDesc(id).stream().map(movimiento -> {
                    MovimientoResponseDTO dto = new MovimientoResponseDTO();
                    dto.setFecha(movimiento.getFecha());
                    dto.setCantidad(movimiento.getCantidad());
                    dto.setTipo(movimiento.getTipo());
                    dto.setId(movimiento.getId());

                    dto.setNombreProducto(movimiento.getProducto().getNombre());

                    return dto;
                } ).toList();
    }



}