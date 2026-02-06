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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "api.security.secret=ClaveSecretaDePruebaParaLosTests1234567890"
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
        // Preparar el usuario mock
        Usuario usuarioMock = new Usuario("Juan", "juan@test.com", "123456");

        Authentication authMock = mock(Authentication.class);
        when(authMock.getPrincipal()).thenReturn(usuarioMock);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);

        // Simulamos el token que genera el servicio
        String tokenSimulado = "token_jwt_simulado_123";
        when(tokenService.generarToken(any(Usuario.class))).thenReturn(tokenSimulado);

        DatosAutenticacionUsuario datosLogin = new DatosAutenticacionUsuario("juan@test.com", "123456");

        mockMvc.perform(post("/auth/login") // ⚠️ Asegúrate que esta ruta sea la correcta
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosLogin)))
                .andDo(print()) // 👈 Esto imprimirá el JSON en el log si falla
                .andExpect(status().isOk())
                // Si tu DTO usa otro nombre (ej. tokenJWT), cámbialo aquí:
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").value(tokenSimulado));
    }
}