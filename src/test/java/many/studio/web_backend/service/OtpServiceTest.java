package many.studio.web_backend.service;

import many.studio.web_backend.dto.usuario.esqueci_senha.RedefinirSenhaOtpRequest;
import many.studio.web_backend.dto.usuario.esqueci_senha.SolicitarOtpRequest;
import many.studio.web_backend.dto.usuario.esqueci_senha.VerificarOtpRequest;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.OtpCodigo;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.exception.OtpInvalidoException;
import many.studio.web_backend.repository.ClienteRepository;
import many.studio.web_backend.repository.OtpCodigoRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import many.studio.web_backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock
    private OtpCodigoRepository otpCodigoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProfissionalRepository profissionalRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NotificacaoPublisher notificacaoPublisher;

    private OtpService otpService;

    private static final String DESTINO = "11999999999";
    private static final String CODIGO = "123456";

    @BeforeEach
    void setUp() {
        otpService = new OtpService(
                otpCodigoRepository,
                usuarioRepository,
                clienteRepository,
                profissionalRepository,
                passwordEncoder,
                notificacaoPublisher);

        ReflectionTestUtils.setField(otpService, "validadeMinutos", 5L);
        ReflectionTestUtils.setField(otpService, "maxTentativas", 5);
        ReflectionTestUtils.setField(otpService, "resetTokenValidadeMinutos", 10L);
    }

    @Test
    void solicitar_deveGerarSalvarEEnviarQuandoTelefoneExiste() {
        Usuario usuario = new Usuario();
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);

        when(otpCodigoRepository.findByDestinoAndUsadoFalse(DESTINO)).thenReturn(List.of());
        when(clienteRepository.findFirstByTelefone(DESTINO)).thenReturn(Optional.of(cliente));

        otpService.solicitar(solicitarRequest(DESTINO));

        verify(otpCodigoRepository).save(any(OtpCodigo.class));
        verify(notificacaoPublisher).enviarWhatsApp(anyString(), anyString());
    }

    @Test
    void solicitar_naoDeveEnviarQuandoTelefoneNaoCadastrado() {
        when(otpCodigoRepository.findByDestinoAndUsadoFalse(DESTINO)).thenReturn(List.of());
        when(clienteRepository.findFirstByTelefone(DESTINO)).thenReturn(Optional.empty());
        when(profissionalRepository.findFirstByTelefone(DESTINO)).thenReturn(Optional.empty());

        otpService.solicitar(solicitarRequest(DESTINO));

        verify(otpCodigoRepository, never()).save(any(OtpCodigo.class));
        verify(notificacaoPublisher, never()).enviarWhatsApp(anyString(), anyString());
    }

    @Test
    void solicitar_deveLancar400QuandoTelefoneInvalido() {
        SolicitarOtpRequest request = solicitarRequest("123");

        assertThrows(ResponseStatusException.class, () -> otpService.solicitar(request));
    }

    @Test
    void verificar_deveEmitirResetTokenQuandoCodigoCorreto() {
        OtpCodigo otp = otpAtivo(CODIGO);
        when(otpCodigoRepository.findFirstByDestinoAndUsadoFalseOrderByCriadoEmDesc(DESTINO))
                .thenReturn(Optional.of(otp));

        String token = otpService.verificar(verificarRequest(DESTINO, CODIGO));

        assertNotNull(token);
        assertTrue(otp.getVerificado());
        assertEquals(token, otp.getResetToken());
        verify(otpCodigoRepository).save(otp);
    }

    @Test
    void verificar_deveIncrementarTentativasQuandoCodigoErrado() {
        OtpCodigo otp = otpAtivo(CODIGO);
        when(otpCodigoRepository.findFirstByDestinoAndUsadoFalseOrderByCriadoEmDesc(DESTINO))
                .thenReturn(Optional.of(otp));

        assertThrows(OtpInvalidoException.class,
                () -> otpService.verificar(verificarRequest(DESTINO, "000000")));

        assertEquals(1, otp.getTentativas());
        verify(otpCodigoRepository).save(otp);
    }

    @Test
    void verificar_deveLancarQuandoCodigoExpirado() {
        OtpCodigo otp = otpAtivo(CODIGO);
        otp.setExpiraEm(LocalDateTime.now().minusMinutes(1));
        when(otpCodigoRepository.findFirstByDestinoAndUsadoFalseOrderByCriadoEmDesc(DESTINO))
                .thenReturn(Optional.of(otp));

        assertThrows(OtpInvalidoException.class,
                () -> otpService.verificar(verificarRequest(DESTINO, CODIGO)));

        assertTrue(otp.getUsado());
    }

    @Test
    void redefinir_deveAtualizarSenhaEEnviarConfirmacao() {
        Usuario usuario = new Usuario();
        usuario.setSenha("senhaAntiga");
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);

        OtpCodigo otp = otpAtivo(CODIGO);
        otp.setVerificado(true);
        otp.setResetToken("token-123");
        otp.setResetTokenExpiraEm(LocalDateTime.now().plusMinutes(10));

        when(otpCodigoRepository
                .findFirstByResetTokenAndUsadoFalseAndVerificadoTrueAndResetTokenExpiraEmAfter(anyString(), any()))
                .thenReturn(Optional.of(otp));
        when(clienteRepository.findFirstByTelefone(DESTINO)).thenReturn(Optional.of(cliente));
        when(passwordEncoder.encode("novaSenha123")).thenReturn("novaSenhaHash");

        otpService.redefinir("token-123", redefinirRequest("novaSenha123"));

        assertEquals("novaSenhaHash", usuario.getSenha());
        assertTrue(otp.getUsado());
        verify(usuarioRepository).save(usuario);
        verify(notificacaoPublisher).enviarWhatsApp(anyString(), anyString());
    }

    @Test
    void redefinir_deveLancarQuandoTokenInvalido() {
        when(otpCodigoRepository
                .findFirstByResetTokenAndUsadoFalseAndVerificadoTrueAndResetTokenExpiraEmAfter(anyString(), any()))
                .thenReturn(Optional.empty());

        assertThrows(OtpInvalidoException.class,
                () -> otpService.redefinir("token-invalido", redefinirRequest("novaSenha123")));
    }

    private OtpCodigo otpAtivo(String codigo) {
        OtpCodigo otp = new OtpCodigo();
        otp.setDestino(DESTINO);
        otp.setCodigoHash(hash(codigo));
        otp.setExpiraEm(LocalDateTime.now().plusMinutes(5));
        otp.setTentativas(0);
        otp.setVerificado(false);
        otp.setUsado(false);
        otp.setCriadoEm(LocalDateTime.now());
        return otp;
    }

    private SolicitarOtpRequest solicitarRequest(String destino) {
        SolicitarOtpRequest request = new SolicitarOtpRequest();
        request.setCanal("TELEFONE");
        request.setDestino(destino);
        return request;
    }

    private VerificarOtpRequest verificarRequest(String destino, String codigo) {
        VerificarOtpRequest request = new VerificarOtpRequest();
        request.setDestino(destino);
        request.setCodigo(codigo);
        return request;
    }

    private RedefinirSenhaOtpRequest redefinirRequest(String novaSenha) {
        RedefinirSenhaOtpRequest request = new RedefinirSenhaOtpRequest();
        request.setNovaSenha(novaSenha);
        return request;
    }

    private String hash(String valor) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(valor.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
