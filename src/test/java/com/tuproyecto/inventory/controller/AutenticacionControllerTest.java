package com.tuproyecto.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuproyecto.inventory.dto.DatosAutenticacionUsuario;
import com.tuproyecto.inventory.model.Usuario;
import com.tuproyecto.inventory.service.AutenticacionService;
import com.tuproyecto.inventory.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// Importaciones estáticas para Mockito y MockMvc
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
        // 1. Datos de entrada (Asegúrate que DatosAutenticacionUsuario use estos nombres)
        DatosAutenticacionUsuario datosLogin = new DatosAutenticacionUsuario("juan@test.com", "123456");

        // 2. Mock del usuario y autenticación
        Usuario usuarioMock = new Usuario("Juan", "juan@test.com", "123456");
        Authentication authMock = mock(Authentication.class);
        when(authMock.getPrincipal()).thenReturn(usuarioMock);

        // 3. Definir comportamiento de los Mocks
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);
        when(tokenService.generarToken(any(Usuario.class)))
                .thenReturn("token_123");

        // 4. Ejecución del test a la ruta "/auth"
        mockMvc.perform(post("/auth")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosLogin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists()); // Verificamos que devuelva cualquier JSON
    }
}