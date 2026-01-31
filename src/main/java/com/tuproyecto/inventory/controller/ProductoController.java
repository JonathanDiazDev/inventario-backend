package com.tuproyecto.inventory.controller;

import com.tuproyecto.inventory.dto.*;
import com.tuproyecto.inventory.model.Producto;
import com.tuproyecto.inventory.model.Categoria; // <--- Importante
import com.tuproyecto.inventory.repository.CategoriaRepository; // <--- Importante
import com.tuproyecto.inventory.repository.MovimientoRepository;
import com.tuproyecto.inventory.repository.ProductoRepository;
import com.tuproyecto.inventory.service.MovimientoService;
import com.tuproyecto.inventory.service.ProductoService;
import jakarta.persistence.EntityNotFoundException; // Para manejar errores
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tuproyecto.inventory.model.Movimiento;
import com.tuproyecto.inventory.model.TipoMovimiento;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository; // <--- Necesario para buscar la categoría

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<DatosListadoProducto>> obtenerProductos() {
        var lista = productoRepository.findAll().stream()
                .map(DatosListadoProducto::new) // Mágicamente convierte Entidad -> DTO
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ CORRECCIÓN 1: Usamos el DTO y buscamos la categoría
    @PostMapping("/movimiento")
    public ResponseEntity<MovimientoResponseDTO> registrarMovimiento(@RequestBody @Valid DatosRegistroMovimiento datos) {
        // 1. Llamamos al Chef (Servicio) y guardamos el resultado
        var movimientoGuardado = movimientoService.registrarMovimiento(datos);

        // 2. Entregamos el resultado al cliente
        return ResponseEntity.ok(new MovimientoResponseDTO(movimientoGuardado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content es lo estándar
    }

    @PutMapping
    @Transactional // Importante: JPA hace el update automático al final del método
    public ResponseEntity<DatosListadoProducto> actualizar(@RequestBody @Valid DatosActualizarProducto datos) {
        // 1. Buscamos el producto
        Producto producto = productoRepository.getReferenceById(datos.id());

        // 2. Actualizamos datos básicos si vienen en el DTO
        if (datos.nombre() != null) producto.setNombre(datos.nombre());
        if (datos.precio() != null) producto.setPrecio(datos.precio());
        if (datos.stock() != null) producto.setStock(datos.stock());

        // 3. LA MAGIA: Actualizamos la categoría si enviaron un ID nuevo
        if (datos.categoriaId() != null) {
            Categoria nuevaCategoria = categoriaRepository.findById(datos.categoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
            producto.setCategoria(nuevaCategoria);
        }

        // 4. Devolvemos el DTO actualizado (ya con la categoría nueva visible)
        return ResponseEntity.ok(new DatosListadoProducto(producto));
    }

    @GetMapping("/buscar")
    public List<Producto> buscarPorNombre(@RequestParam String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }


    // ✅ CORRECCIÓN 2: Mapeo correcto para el Record
    @GetMapping("/{id}/movimientos")
    public List<MovimientoResponseDTO> obtenerHistorial(@PathVariable Long id) {
        return movimientoRepository.findByProductoIdOrderByFechaDesc(id)
                .stream()
                .map(MovimientoResponseDTO::new) // <--- ¡MIRA QUÉ LIMPIO! Usa el constructor que creamos
                .toList();
    }
}