package com.tuproyecto.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuproyecto.inventory.dto.DatosAutenticacionUsuario;
import com.tuproyecto.inventory.service.TokenService;
import com.tuproyecto.inventory.model.Usuario;
import com.tuproyecto.inventory.service.AutenticacionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

// Imports estáticos necesarios
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "api.security.secret=ClaveSecretaDePruebaParaLosTests1234567890",
        "spring.main.allow-bean-definition-overriding=true" // Permite sobrescribir beans en tests
})
class AutenticacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private AutenticacionService autenticacionService;

    @Test
    void debeDevolverTokenCuandoCredencialesSonValidas() throws Exception {
        // Preparar datos
        DatosAutenticacionUsuario datosLogin = new DatosAutenticacionUsuario("juan@test.com", "123456");
        Usuario usuarioMock = new Usuario("Juan", "juan@test.com", "123456");

        Authentication authMock = mock(Authentication.class);
        when(authMock.getPrincipal()).thenReturn(usuarioMock);
        when(authMock.isAuthenticated()).thenReturn(true); // Forzamos que esté autenticado

        // Mock del manager
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);

        // Mock del token
        when(tokenService.generarToken(any(Usuario.class))).thenReturn("token_jwt_simulado_123");

        // Ejecutar petición
        mockMvc.perform(post("/auth") // Si tu ruta es /auth, cámbiala aquí
                        .with(csrf())   // Protege contra el 403 de CSRF
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosLogin)))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print()) // Imprime el error real en consola
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token_jwt_simulado_123"));
    }
}