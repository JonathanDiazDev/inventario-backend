package com.tuproyecto.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.tuproyecto.inventory.dto.DatosAutenticacionUsuario;
import com.tuproyecto.inventory.model.Usuario;
import com.tuproyecto.inventory.service.AutenticacionService;
import com.tuproyecto.inventory.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "api.security.secret=ClaveSecretaDePruebaParaLosTests1234567890"
})
@WebMvcTest(AutenticacionController.class) // 1. Solo cargamos el Controller
class AutenticacionControllerTest {

    @Autowired
    private MockMvc mockMvc; // El "Robot" que hace peticiones

    @Autowired
    private ObjectMapper objectMapper; // Convierte Objetos a JSON

    // 3. Mocks de las dependencias que pide el Controller
    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private AutenticacionService autenticacionService;

    @Test
    void debeDevolverTokenCuandoCredencialesSonValidas() throws Exception {
        // ARRANGE (Preparar)
        DatosAutenticacionUsuario datosLogin = new DatosAutenticacionUsuario("juan@test.com", "123456");

        // Simulamos que la autenticación es exitosa
        Authentication authMock = mock(Authentication.class);
        Usuario usuarioMock = new Usuario("Juan", "juan@test.com", "clave");
        when(authMock.getPrincipal()).thenReturn(usuarioMock);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);

        // Simulamos que se genera un token
        when(tokenService.generarToken(any(Usuario.class))).thenReturn("token_jwt_simulado_123");

        // ACT & ASSERT (Actuar y Verificar)
        mockMvc.perform(post("/auth") // Hacemos POST a /auth
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosLogin))) // Enviamos el JSON
                .andExpect(status().isOk()) // Esperamos HTTP 200
                .andExpect(jsonPath("$.token").value("token_jwt_simulado_123")); // Esperamos el JSON de respuesta
    }
}