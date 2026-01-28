package com.tuproyecto.inventory.controller;

import com.tuproyecto.inventory.dto.DatosAutenticacionUsuario;
import com.tuproyecto.inventory.dto.DatosJWTToken; // Asegúrate de tener este DTO
import com.tuproyecto.inventory.model.Usuario;
import com.tuproyecto.inventory.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService; // <--- Traemos el servicio aquí, al nivel del controlador

    @PostMapping
    public ResponseEntity autenticarUsuario(@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario) {
        // 1. Creamos el token de autenticación con los datos del JSON
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                datosAutenticacionUsuario.email(),
                datosAutenticacionUsuario.password()
        );

        // 2. Validamos al usuario (aquí es donde Spring Security hace su magia)
        var usuarioAutenticado = authenticationManager.authenticate(authToken);

        // 3. Si todo está bien, generamos el JWT Token
        var jwtToken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());

        // 4. Devolvemos el token envuelto en el DTO
        return ResponseEntity.ok(new DatosJWTToken(jwtToken));
    }
}