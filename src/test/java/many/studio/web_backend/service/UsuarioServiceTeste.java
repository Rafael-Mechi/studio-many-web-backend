package many.studio.web_backend.service;

import jakarta.persistence.EntityNotFoundException;
import many.studio.web_backend.dto.usuario.UsuarioAtualizarPerfilDto;
import many.studio.web_backend.dto.usuario.UsuarioAtualizarSenhaDto;
import many.studio.web_backend.dto.usuario.UsuarioRedefinirSenhaDto;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.Perfil;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.repository.ClienteRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import many.studio.web_backend.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProfissionalRepository profissionalRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private Perfil perfilCliente;

    @BeforeEach
    void setUp() {
        perfilCliente = new Perfil();
        perfilCliente.setId(3);
        perfilCliente.setPerfil("ROLE_CLIENTE");

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("giovana@email.com");
        usuario.setSenha("hash-antigo");
        usuario.setPerfil(perfilCliente);
        usuario.setAtivo(true);
    }

    @AfterEach
    void limparSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private void autenticarComo(String email) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                email,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void atualizarSenha_deveAtualizarQuandoSenhaAtualCorreta() {
        autenticarComo("giovana@email.com");
        UsuarioAtualizarSenhaDto dto = new UsuarioAtualizarSenhaDto();
        dto.setSenhaAtual("123456");
        dto.setSenhaNova("novaSenha789");

        when(usuarioRepository.findByEmail("giovana@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123456", "hash-antigo")).thenReturn(true);
        when(passwordEncoder.encode("novaSenha789")).thenReturn("hash-novo");

        usuarioService.atualizarSenha(dto);

        verify(passwordEncoder).matches("123456", "hash-antigo");
        verify(passwordEncoder).encode("novaSenha789");
        verify(usuarioRepository).save(usuario);
        assertEquals("hash-novo", usuario.getSenha());
    }

    @Test
    void atualizarSenha_deveLancar401QuandoSenhaAtualInvalida() {
        autenticarComo("giovana@email.com");
        UsuarioAtualizarSenhaDto dto = new UsuarioAtualizarSenhaDto();
        dto.setSenhaAtual("errada");
        dto.setSenhaNova("novaSenha789");

        when(usuarioRepository.findByEmail("giovana@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("errada", "hash-antigo")).thenReturn(false);

        var ex = assertThrows(ResponseStatusException.class, () -> usuarioService.atualizarSenha(dto));

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void atualizarSenha_deveLancar401QuandoNaoAutenticado() {
        UsuarioAtualizarSenhaDto dto = new UsuarioAtualizarSenhaDto();
        dto.setSenhaAtual("123456");
        dto.setSenhaNova("novaSenha789");

        var ex = assertThrows(ResponseStatusException.class, () -> usuarioService.atualizarSenha(dto));

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void redefinirSenha_deveAtualizarSenhaQuandoEmailExiste() {
        UsuarioRedefinirSenhaDto dto = new UsuarioRedefinirSenhaDto();
        dto.setEmail("giovana@email.com");
        dto.setSenhaNova("senhaNova123");

        when(usuarioRepository.findByEmail("giovana@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("senhaNova123")).thenReturn("hash-novo");

        usuarioService.redefinirSenha(dto);

        verify(usuarioRepository).save(usuario);
        assertEquals("hash-novo", usuario.getSenha());
    }

    @Test
    void redefinirSenha_deveLancar404QuandoEmailNaoExiste() {
        UsuarioRedefinirSenhaDto dto = new UsuarioRedefinirSenhaDto();
        dto.setEmail("naoexiste@email.com");
        dto.setSenhaNova("senhaNova123");

        when(usuarioRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioService.redefinirSenha(dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void atualizarPerfil_deveLancar400QuandoBodyVazio() {
        UsuarioAtualizarPerfilDto dto = new UsuarioAtualizarPerfilDto();

        var ex = assertThrows(ResponseStatusException.class, () -> usuarioService.atualizarPerfil(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void atualizarPerfil_deveAtualizarNomeETelefoneDoCliente() {
        autenticarComo("giovana@email.com");
        UsuarioAtualizarPerfilDto dto = new UsuarioAtualizarPerfilDto();
        dto.setNome("Giovana Lopes");
        dto.setTelefone("11999998888");

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);

        when(usuarioRepository.findByEmail("giovana@email.com")).thenReturn(Optional.of(usuario));
        when(clienteRepository.findByUsuario_Id(1)).thenReturn(Optional.of(cliente));

        usuarioService.atualizarPerfil(dto);

        assertEquals("Giovana Lopes", cliente.getNome());
        assertEquals("11999998888", cliente.getTelefone());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void atualizarPerfil_deveLancar409QuandoEmailJaCadastrado() {
        autenticarComo("giovana@email.com");
        UsuarioAtualizarPerfilDto dto = new UsuarioAtualizarPerfilDto();
        dto.setEmail("outro@email.com");

        when(usuarioRepository.findByEmail("giovana@email.com")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmail("outro@email.com")).thenReturn(true);

        assertThrows(EntityConflictException.class, () -> usuarioService.atualizarPerfil(dto));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void atualizarPerfil_deveAtualizarNomeDoProfissional() {
        Perfil perfilProfissional = new Perfil();
        perfilProfissional.setId(2);
        perfilProfissional.setPerfil("ROLE_PROFISSIONAL");
        usuario.setPerfil(perfilProfissional);
        usuario.setEmail("ana@email.com");

        autenticarComo("ana@email.com");

        UsuarioAtualizarPerfilDto dto = new UsuarioAtualizarPerfilDto();
        dto.setNome("Ana Silva");

        Profissional profissional = new Profissional();
        profissional.setUsuario(usuario);

        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(usuario));
        when(profissionalRepository.findByUsuario_Id(1)).thenReturn(Optional.of(profissional));

        usuarioService.atualizarPerfil(dto);

        assertEquals("Ana Silva", profissional.getNome());
        verify(profissionalRepository).save(profissional);
    }
}