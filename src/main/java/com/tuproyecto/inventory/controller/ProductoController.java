package com.tuproyecto.inventory.controller;

import com.tuproyecto.inventory.model.Producto;
import com.tuproyecto.inventory.repository.ProductoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Le dice a Spring que esto es una API
@RequestMapping("/api/productos") // La dirección web para entrar aquí
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

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

}