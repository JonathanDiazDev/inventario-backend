package com.tuproyecto.inventory.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tuproyecto.inventory.model.Usuario;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    public String generarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("123456");
            return JWT.create()
                    .withIssuer("api inventory")
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar token", e);
        }

    }

    private Instant generarFechaExpiracion() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-06:00"));
    }

    public String getSubject(String token) {
        try {
            var algoritmo = Algorithm.HMAC256("123456");
            return JWT.require(algoritmo)
                    .withIssuer("api inventory")
                    .build()
                    .verify(token)
                    .getSubject();

        }catch (JWTVerificationException exception){
            System.out.println("🚨 ERROR VERIFICANDO TOKEN: " + exception.getMessage());
            return null;
        }
    }
}
