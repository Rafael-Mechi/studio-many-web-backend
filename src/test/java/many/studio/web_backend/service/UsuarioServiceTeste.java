package many.studio.web_backend.service;

import many.studio.web_backend.dto.usuario.UsuarioCriacaoDto;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.dto.usuario.UsuarioAtualizarPerfilDto;
import many.studio.web_backend.dto.usuario.UsuarioAtualizarSenhaDto;
import many.studio.web_backend.dto.usuario.UsuarioRedefinirSenhaDto;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.Perfil;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.repository.*;
import many.studio.web_backend.strategy.ProfissionalCriacaoStrategy;
import many.studio.web_backend.strategy.UsuarioCriacaoStrategy;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTeste {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProfissionalRepository profissionalRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private UsuarioCriacaoStrategy usuarioCriacaoStrategy;

    @Mock
    private List<UsuarioCriacaoStrategy> strategies;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private ProfissionalCriacaoStrategy profissionalCriacaoStrategy;

    @Mock
    private ServicoProfissionalRepository servicoProfissionalRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private Cliente cliente;

    private Perfil perfilAdmin;
    private Perfil perfilFuncionario;
    private Perfil perfilCliente;

    @BeforeEach
    void setUp() {
        perfilAdmin = new Perfil();
        perfilAdmin.setId(1L);
        perfilAdmin.setPerfil("ROLE_ADMIN");

        perfilFuncionario = new Perfil();
        perfilFuncionario.setId(2L);
        perfilFuncionario.setPerfil("ROLE_FUNCIONARIO");

        perfilCliente = new Perfil();
        perfilCliente.setId(3L);
        perfilCliente.setPerfil("ROLE_CLIENTE");

        usuario = new Usuario();
        usuario.setId(1L);
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
    void deveCriarUsuarioClienteComExito() {
        // given
        UsuarioCriacaoDto dto = new UsuarioCriacaoDto();
        dto.setNome("noa");
        dto.setEmail("noa@gmail.com");
        dto.setSenha("123456");
        dto.setTelefone("11999999999");
        dto.setDocumento("12345678900");
        dto.setPerfilId(3L);

        // when
        when(usuarioRepository.existsByEmail(dto.getEmail()))
                .thenReturn(false);

        when(perfilRepository.findById(dto.getPerfilId()))
                .thenReturn(Optional.of(perfilCliente));

        when(passwordEncoder.encode(dto.getSenha()))
                .thenReturn("2132adsrf");

        when(usuarioCriacaoStrategy.suporta(dto.getPerfilId()))
                .thenReturn(true);

        when(strategies.stream()).thenReturn(Stream.of(usuarioCriacaoStrategy));

        usuarioService.criar(dto);

        // then
        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioCriacaoStrategy).criarDadosComplementares(
                any(Usuario.class),
                any(UsuarioCriacaoDto.class)
        );
        verify(passwordEncoder).encode(dto.getSenha());
    }

    @Test
    void deveCriarUsuarioFuncionarioComExito() {
        // given
        UsuarioCriacaoDto dto = new UsuarioCriacaoDto();
        dto.setNome("Giovana");
        dto.setEmail("giovana@email.com");
        dto.setSenha("123456");
        dto.setPerfilId(perfilFuncionario.getId());

        when(usuarioRepository.existsByEmail(dto.getEmail()))
                .thenReturn(false);

        when(perfilRepository.findById(dto.getPerfilId()))
                .thenReturn(Optional.of(perfilFuncionario));

        when(passwordEncoder.encode(dto.getSenha()))
                .thenReturn("senha-criptografada");

        when(usuarioCriacaoStrategy.suporta(dto.getPerfilId()))
                .thenReturn(true);

        // Configure a lista de strategies
        when(strategies.stream()).thenReturn(Stream.of(usuarioCriacaoStrategy));

        // when
        usuarioService.criar(dto);

        // then
        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioCriacaoStrategy).criarDadosComplementares(
                any(Usuario.class),
                any(UsuarioCriacaoDto.class)
        );
        verify(passwordEncoder).encode(dto.getSenha());
    }

    @Test
    void deveCriarUsuarioAdminComExito() {
        // given
        UsuarioCriacaoDto dto = new UsuarioCriacaoDto();
        dto.setNome("Giovana");
        dto.setEmail("giovana@email.com");
        dto.setSenha("123456");
        dto.setPerfilId(perfilAdmin.getId());

        // when
        when(usuarioRepository.existsByEmail(dto.getEmail()))
                .thenReturn(false);

        when(perfilRepository.findById(dto.getPerfilId()))
                .thenReturn(Optional.of(perfilAdmin));

        when(passwordEncoder.encode(dto.getSenha()))
                .thenReturn("senha-criptografada");

        when(usuarioCriacaoStrategy.suporta(dto.getPerfilId()))
                .thenReturn(true);

        when(strategies.stream()).thenReturn(Stream.of(usuarioCriacaoStrategy));

        // then

        usuarioService.criar(dto);


        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioCriacaoStrategy).criarDadosComplementares(
                any(Usuario.class),
                any(UsuarioCriacaoDto.class)
        );
        verify(passwordEncoder).encode(dto.getSenha());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExistir() {
        // given
        UsuarioCriacaoDto dto = new UsuarioCriacaoDto();
        dto.setEmail("giovana@email.com");

        // when

        when(usuarioRepository.existsByEmail(dto.getEmail()))
                .thenReturn(true);

        // then
        assertThrows(EntityConflictException.class,
                () -> usuarioService.criar(dto));

        verify(usuarioRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
        verify(usuarioCriacaoStrategy, never())
                .criarDadosComplementares(any(), any());


    }

    @Test
    void deveLancarExcecaoQuandoPerfilNaoExistir() {
        // given
        UsuarioCriacaoDto dto = new UsuarioCriacaoDto();
        dto.setNome("noa");
        dto.setEmail("noa@gmail.com");
        dto.setSenha("123456");
        dto.setPerfilId(999L);

        // when

        when(usuarioRepository.existsByEmail(dto.getEmail()))
                .thenReturn(false);

        when(perfilRepository.findById(dto.getPerfilId()))
                .thenReturn(Optional.empty());


        // then
        assertThrows(EntityNotFoundException.class,
                () -> usuarioService.criar(dto));

        verify(usuarioRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void deveLancarExcecaoQuandoNenhumaStrategySuportarPerfil() {
        // given
        UsuarioCriacaoDto dto = new UsuarioCriacaoDto();
        dto.setNome("noa");
        dto.setEmail("noa@gmail.com");
        dto.setSenha("123456");
        dto.setPerfilId(3L);

        // when

        when(usuarioRepository.existsByEmail(dto.getEmail()))
                .thenReturn(false);

        when(perfilRepository.findById(dto.getPerfilId()))
                .thenReturn(Optional.of(perfilCliente));

        when(passwordEncoder.encode(dto.getSenha()))
                .thenReturn("senha-criptografada");

        when(usuarioCriacaoStrategy.suporta(dto.getPerfilId()))
                .thenReturn(false);

        when(strategies.stream()).thenReturn(Stream.of(usuarioCriacaoStrategy));

        // then
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.criar(dto));

        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioCriacaoStrategy, never())
                .criarDadosComplementares(any(), any());
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
        when(clienteRepository.findByUsuario_Id(1L)).thenReturn(Optional.of(cliente));

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
        perfilProfissional.setId(2L);
        perfilProfissional.setPerfil("ROLE_PROFISSIONAL");
        usuario.setPerfil(perfilProfissional);
        usuario.setEmail("ana@email.com");

        autenticarComo("ana@email.com");

        UsuarioAtualizarPerfilDto dto = new UsuarioAtualizarPerfilDto();
        dto.setNome("Ana Silva");

        Profissional profissional = new Profissional();
        profissional.setUsuario(usuario);

        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(usuario));
        when(profissionalRepository.findByUsuario_Id(1L)).thenReturn(Optional.of(profissional));

        usuarioService.atualizarPerfil(dto);

        assertEquals("Ana Silva", profissional.getNome());
        verify(profissionalRepository).save(profissional);
    }
}