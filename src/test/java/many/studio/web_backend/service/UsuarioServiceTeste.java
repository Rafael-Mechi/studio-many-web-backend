package many.studio.web_backend.service;

import many.studio.web_backend.dto.usuario.*;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.entity.Perfil;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.repository.*;
import many.studio.web_backend.strategy.UsuarioCriacaoStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
    private ServicoProfissionalRepository servicoProfissionalRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

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
        perfilFuncionario.setPerfil("ROLE_PROFISSIONAL");

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
    void removerPorId_deveRemoverUsuarioQuandoExistir() {
        // given
        when(usuarioRepository.existsById(1L))
                .thenReturn(true);

        // when
        usuarioService.removerPorId(1L);

        // then
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void removerPorId_deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // given
        when(usuarioRepository.existsById(1L))
                .thenReturn(false);

        // then
        assertThrows(EntityNotFoundException.class,
                () -> usuarioService.removerPorId(1L));

        verify(usuarioRepository, never()).deleteById(any());
    }
}
