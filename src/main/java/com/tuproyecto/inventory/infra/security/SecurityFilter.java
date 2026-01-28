package com.tuproyecto.inventory.infra.security;


import com.tuproyecto.inventory.repository.UsuarioRepository;
import com.tuproyecto.inventory.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var authHeader = request.getHeader("Authorization");

        System.out.println("Filtro ejecutándose. Header recibido: " + authHeader);
        if (authHeader != null) {
            var token = authHeader.replace("Bearer ", "");
            var nombreUsuario = tokenService.getSubject(token);
            System.out.println("Usuario detectado en token: " + nombreUsuario);
            if (nombreUsuario != null) {
                var usuario = usuarioRepository.findByEmail(nombreUsuario).get();
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Autenticación establecida en el contexto para: " + nombreUsuario);
            }

        }
        filterChain.doFilter(request, response);
    }
}
