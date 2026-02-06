package com.tuproyecto.inventory.controller;

import com.tuproyecto.inventory.dto.DatosAutenticacionUsuario;
import com.tuproyecto.inventory.dto.DatosJWTToken; // Asegúrate de tener este DTO
import com.tuproyecto.inventory.dto.DatosRegistroUsuario;
import com.tuproyecto.inventory.model.Usuario;
import com.tuproyecto.inventory.service.AutenticacionService;
import com.tuproyecto.inventory.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AutenticacionController {


    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final AutenticacionService autenticacionService;

    public AutenticacionController(AuthenticationManager authenticationManager, TokenService tokenService, AutenticacionService autenticacionService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.autenticacionService = autenticacionService;
    }


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

    @PostMapping("/register")
    public ResponseEntity registrarUsuario(@RequestBody @Valid DatosRegistroUsuario datos) {
        // Delegamos al experto (Service)
        String respuesta = autenticacionService.registrar(datos);
        return ResponseEntity.ok(respuesta);
    }

}