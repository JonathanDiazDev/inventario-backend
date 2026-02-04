package com.tuproyecto.inventory.controller;

import com.tuproyecto.inventory.dto.DatosAutenticacionUsuario;
import com.tuproyecto.inventory.dto.DatosJWTToken; // Asegúrate de tener este DTO
import com.tuproyecto.inventory.dto.DatosRegistroUsuario;
import com.tuproyecto.inventory.model.Usuario;
import com.tuproyecto.inventory.repository.UsuarioRepository;
import com.tuproyecto.inventory.service.TokenService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AutenticacionController {


    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AutenticacionController(AuthenticationManager authenticationManager, TokenService tokenService, UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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
    @Transactional
    public ResponseEntity registrarUsuario(@RequestBody @Valid DatosRegistroUsuario datos) {
        // 1. Encriptamos la contraseña
        String claveEncriptada = passwordEncoder.encode(datos.password());

        // 2. Creamos el usuario con la clave YA encriptada
        Usuario usuario = new Usuario(datos.nombre(), datos.email(), claveEncriptada);

        // 3. Guardamos
        usuarioRepository.save(usuario);

        // 4. Retornamos algo (o un token directo para que quede logueado)
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }


}