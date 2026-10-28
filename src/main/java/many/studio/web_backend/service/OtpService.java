package many.studio.web_backend.service;

import many.studio.web_backend.dto.usuario.esqueci_senha.RedefinirSenhaOtpRequest;
import many.studio.web_backend.dto.usuario.esqueci_senha.SolicitarOtpRequest;
import many.studio.web_backend.dto.usuario.esqueci_senha.VerificarOtpRequest;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.OtpCodigo;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.exception.OtpInvalidoException;
import many.studio.web_backend.repository.ClienteRepository;
import many.studio.web_backend.repository.OtpCodigoRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import many.studio.web_backend.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Recuperacao de senha via codigo OTP enviado por WhatsApp. O codigo em claro
 * nunca e persistido - apenas o hash SHA-256. O envio efetivo e delegado ao
 * microservico de acoes via RabbitMQ.
 */
@Service
public class OtpService {

    private static final Logger log = LoggerFactory.getLogger(OtpService.class);

    private static final int TAMANHO_CODIGO = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final OtpCodigoRepository otpCodigoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificacaoPublisher notificacaoPublisher;

    @Value("${otp.validade-minutos}")
    private long validadeMinutos;

    @Value("${otp.max-tentativas}")
    private int maxTentativas;

    @Value("${otp.reset-token-validade-minutos}")
    private long resetTokenValidadeMinutos;

    public OtpService(OtpCodigoRepository otpCodigoRepository,
            UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository,
            ProfissionalRepository profissionalRepository,
            PasswordEncoder passwordEncoder,
            NotificacaoPublisher notificacaoPublisher) {
        this.otpCodigoRepository = otpCodigoRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.profissionalRepository = profissionalRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificacaoPublisher = notificacaoPublisher;
    }

    /**
     * Gera e envia o codigo OTP. Nunca revela se o telefone existe: quando nao ha
     * usuario, retorna silenciosamente sem enviar nada.
     */
    public void solicitar(SolicitarOtpRequest dto) {
        String destino = somenteDigitos(dto.getDestino());
        if (destino.length() < 10 || destino.length() > 11) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone invalido");
        }

        invalidarPendentes(destino);

        if (localizarUsuario(destino).isEmpty()) {
            log.info("Solicitacao de OTP para telefone sem usuario cadastrado");
            return;
        }

        String codigo = gerarCodigo();

        OtpCodigo otp = new OtpCodigo();
        otp.setDestino(destino);
        otp.setCodigoHash(hash(codigo));
        otp.setExpiraEm(LocalDateTime.now().plusMinutes(validadeMinutos));
        otp.setTentativas(0);
        otp.setVerificado(false);
        otp.setUsado(false);
        otp.setCriadoEm(LocalDateTime.now());
        otpCodigoRepository.save(otp);

        notificacaoPublisher.enviarWhatsApp(destino,
                "🔐 Seu código de verificação é: *" + codigo
                        + "*\nUse este código para confirmar sua identidade. Ele expira em " + validadeMinutos + " minutos.\n ⚠️ *Não compartilhe* este código com ninguém.");
    }

    /**
     * Valida o codigo e, em caso de sucesso, emite o token de redefinicao.
     */
    public String verificar(VerificarOtpRequest dto) {
        String destino = somenteDigitos(dto.getDestino());
        String codigo = somenteDigitos(dto.getCodigo());

        if (destino.isBlank() || codigo.length() != TAMANHO_CODIGO) {
            throw new OtpInvalidoException("Codigo invalido ou expirado");
        }

        OtpCodigo otp = otpCodigoRepository
                .findFirstByDestinoAndUsadoFalseOrderByCriadoEmDesc(destino)
                .orElseThrow(() -> new OtpInvalidoException("Codigo invalido ou expirado"));

        if (Boolean.TRUE.equals(otp.getVerificado())) {
            throw new OtpInvalidoException("Codigo invalido ou expirado");
        }

        if (otp.getExpiraEm().isBefore(LocalDateTime.now())) {
            otp.setUsado(true);
            otpCodigoRepository.save(otp);
            throw new OtpInvalidoException("Codigo invalido ou expirado");
        }

        int tentativas = otp.getTentativas() == null ? 0 : otp.getTentativas();
        if (tentativas >= maxTentativas) {
            otp.setUsado(true);
            otpCodigoRepository.save(otp);
            throw new OtpInvalidoException("Codigo invalido ou expirado");
        }

        if (!hash(codigo).equals(otp.getCodigoHash())) {
            otp.setTentativas(tentativas + 1);
            otpCodigoRepository.save(otp);
            throw new OtpInvalidoException("Codigo invalido ou expirado");
        }

        String resetToken = UUID.randomUUID().toString();
        otp.setVerificado(true);
        otp.setResetToken(resetToken);
        otp.setResetTokenExpiraEm(LocalDateTime.now().plusMinutes(resetTokenValidadeMinutos));
        otpCodigoRepository.save(otp);

        return resetToken;
    }

    /**
     * Redefine a senha usando o token emitido na verificacao e notifica o cliente.
     */
    public void redefinir(String resetToken, RedefinirSenhaOtpRequest dto) {
        if (resetToken == null || resetToken.isBlank()) {
            throw new OtpInvalidoException("Sessao de redefinicao invalida ou expirada");
        }

        OtpCodigo otp = otpCodigoRepository
                .findFirstByResetTokenAndUsadoFalseAndVerificadoTrueAndResetTokenExpiraEmAfter(
                        resetToken, LocalDateTime.now())
                .orElseThrow(() -> new OtpInvalidoException("Sessao de redefinicao invalida ou expirada"));

        Usuario usuario = localizarUsuario(otp.getDestino())
                .orElseThrow(() -> new OtpInvalidoException("Sessao de redefinicao invalida ou expirada"));

        usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        usuarioRepository.save(usuario);

        otp.setUsado(true);
        otp.setResetToken(null);
        otpCodigoRepository.save(otp);

        notificacaoPublisher.enviarWhatsApp(otp.getDestino(),
                "Sua senha do Studio Essência foi redefinida com sucesso.");
    }

    private void invalidarPendentes(String destino) {
        List<OtpCodigo> pendentes = otpCodigoRepository.findByDestinoAndUsadoFalse(destino);
        if (pendentes.isEmpty()) {
            return;
        }
        pendentes.forEach(otp -> otp.setUsado(true));
        otpCodigoRepository.saveAll(pendentes);
    }

    private Optional<Usuario> localizarUsuario(String destino) {
        Optional<Cliente> cliente = clienteRepository.findFirstByTelefone(destino);
        if (cliente.isPresent()) {
            return Optional.ofNullable(cliente.get().getUsuario());
        }
        return profissionalRepository.findFirstByTelefone(destino)
                .map(Profissional::getUsuario);
    }

    private String gerarCodigo() {
        int numero = RANDOM.nextInt(1_000_000);
        return String.format("%0" + TAMANHO_CODIGO + "d", numero);
    }

    private String somenteDigitos(String valor) {
        return valor == null ? "" : valor.replaceAll("\\D", "");
    }

    private String hash(String valor) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(valor.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Algoritmo de hash indisponivel", ex);
        }
    }
}
