package many.studio.web_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import many.studio.web_backend.dto.usuario.esqueci_senha.RedefinirSenhaOtpRequest;
import many.studio.web_backend.dto.usuario.esqueci_senha.SolicitarOtpRequest;
import many.studio.web_backend.dto.usuario.esqueci_senha.VerificarOtpRequest;
import many.studio.web_backend.dto.usuario.esqueci_senha.VerificarOtpResponse;
import many.studio.web_backend.service.OtpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

/**
 * Fluxo publico de recuperacao de senha por codigo OTP (WhatsApp).
 */
@RestController
@RequestMapping("/usuarios/esqueci-senha")
public class EsqueciSenhaController {

    public static final String COOKIE_RESET_NOME = "resetToken";

    private final OtpService otpService;

    @Value("${otp.reset-token-validade-minutos}")
    private long resetTokenValidadeMinutos;

    public EsqueciSenhaController(OtpService otpService) {
        this.otpService = otpService;
    }

    @Operation(summary = "Solicitar codigo OTP por WhatsApp")
    @PostMapping("/solicitar")
    public ResponseEntity<Void> solicitar(@Valid @RequestBody SolicitarOtpRequest dto) {
        otpService.solicitar(dto);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Verificar codigo OTP")
    @PostMapping("/verificar")
    public ResponseEntity<VerificarOtpResponse> verificar(
            @Valid @RequestBody VerificarOtpRequest dto,
            HttpServletResponse response) {

        String resetToken = otpService.verificar(dto);

        ResponseCookie cookie = ResponseCookie.from(COOKIE_RESET_NOME, resetToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/usuarios/esqueci-senha")
                .maxAge(Duration.ofMinutes(resetTokenValidadeMinutos))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new VerificarOtpResponse(resetToken));
    }

    @Operation(summary = "Redefinir senha com token de verificacao")
    @PostMapping("/redefinir")
    public ResponseEntity<Void> redefinir(
            @CookieValue(name = COOKIE_RESET_NOME, required = false) String resetToken,
            @Valid @RequestBody RedefinirSenhaOtpRequest dto,
            HttpServletResponse response) {

        otpService.redefinir(resetToken, dto);

        ResponseCookie cookie = ResponseCookie.from(COOKIE_RESET_NOME, "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/usuarios/esqueci-senha")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.noContent().build();
    }
}
