package com.tuproyecto.inventory.controller;
import com.tuproyecto.inventory.model.Categoria;
import com.tuproyecto.inventory.repository.CategoriaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Le dice a Spring que esto es una API
@RequestMapping("/api/categorias") // La dirección web para entrar aquí
public class CategoriaController {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Categoria> obtenerCategorias(@RequestParam (required = false) String nombre) {
        if (nombre != null) {
            return categoriaRepository.findByNombreContainingIgnoreCase(nombre);
        } else {
            return categoriaRepository.findAll();
        }

    }
    @PostMapping
    public Categoria guardarCategoria(@Valid @RequestBody Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaRepository.deleteById(id);

    }

}