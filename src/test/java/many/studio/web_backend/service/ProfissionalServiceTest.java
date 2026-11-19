package many.studio.web_backend.service;

import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfissionalServiceTest {

    @Mock
    private ProfissionalRepository profissionalRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProfissionalService profissionalService;

    private Profissional profissional;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("carlos@email.com");
        usuario.setSenha("senha123");
        usuario.setAtivo(true);

        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setNome("Carlos");
        profissional.setUsuario(usuario);
    }

    @Nested
    @DisplayName("Testes do método deletarProfissional (Soft Delete)")
    class DeletarProfissionalTests {

        @Test
        @DisplayName("Deve realizar o soft delete inativando o campo ativo do usuário")
        void deveRealizarSoftDeleteInativandoUsuario() {
            when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));

            profissionalService.deletarProfissional(1L);

            assertFalse(profissional.getUsuario().getAtivo());
            verify(profissionalRepository, times(1)).save(profissional);
        }
    }
}
