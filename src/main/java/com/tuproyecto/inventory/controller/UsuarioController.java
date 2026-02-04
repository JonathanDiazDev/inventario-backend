package com.tuproyecto.inventory.controller;

import com.tuproyecto.inventory.dto.DatosRegistroUsuario;
import com.tuproyecto.inventory.model.Usuario;
import com.tuproyecto.inventory.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroUsuario datos){

        String passwordEncriptada = passwordEncoder.encode(datos.password());

        Usuario usuario = new Usuario(datos.nombre(), datos.email(), passwordEncriptada);

        repository.save(usuario);

        return ResponseEntity.ok().build();

    }
}
