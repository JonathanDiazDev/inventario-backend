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
        // ARRANGE
        DatosAutenticacionUsuario datosLogin = new DatosAutenticacionUsuario("juan@test.com", "123456");

        // ✅ USAMOS EL CONSTRUCTOR QUE YA TIENES (Nombre, Login, Clave)
        // Esto soluciona el error en rojo del .setLogin
        Usuario usuarioMock = new Usuario("Juan", "juan@test.com", "123456");

        Authentication authMock = mock(Authentication.class);
        when(authMock.getPrincipal()).thenReturn(usuarioMock);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);

        when(tokenService.generarToken(any(Usuario.class))).thenReturn("token_jwt_simulado_123");

        // ACT & ASSERT
        // Asegúrate de que tu ruta sea /login o /auth. Si tu controller dice /login, deja esto así:
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosLogin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token_jwt_simulado_123"));
    }
}