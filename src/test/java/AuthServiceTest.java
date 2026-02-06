import com.tuproyecto.inventory.dto.DatosRegistroUsuario;
import com.tuproyecto.inventory.repository.UsuarioRepository;
import com.tuproyecto.inventory.service.AutenticacionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AutenticacionServiceTest { // Cambiado el nombre de la clase de test

    @Mock
    private UsuarioRepository repository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private AutenticacionService autenticacionService; // Usando tu nombre real

    @Test
    void debeRegistrarUsuarioExitosamente() {
        // 1. Revisa si tu DTO se llama RegistroDTO o DatosRegistroUsuario
        var dto = new DatosRegistroUsuario("Juan", "juan@test.com", "123456");

        // 2. Simulamos el comportamiento del encoder
        when(encoder.encode(anyString())).thenReturn("password_encriptada");

        // 3. Ejecutamos el método usando el nombre de tu service
        var resultado = autenticacionService.registrar(dto);

        // 4. Verificaciones
        assertEquals("Usuario registrado exitosamente", resultado);
        verify(repository, times(1)).save(any());
    }
}