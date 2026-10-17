package many.studio.web_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Codigo de verificacao (OTP) para recuperacao de senha. O codigo em claro nunca
 * e persistido - apenas o hash SHA-256.
 */
@Entity
@Table(name = "otp_codigos")
public class OtpCodigo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "destino")
    private String destino;

    @Column(name = "codigo_hash")
    private String codigoHash;

    @Column(name = "expira_em")
    private LocalDateTime expiraEm;

    @Column(name = "tentativas")
    private Integer tentativas;

    @Column(name = "verificado")
    private Boolean verificado;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expira_em")
    private LocalDateTime resetTokenExpiraEm;

    @Column(name = "usado")
    private Boolean usado;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    public OtpCodigo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public String getCodigoHash() { return codigoHash; }
    public void setCodigoHash(String codigoHash) { this.codigoHash = codigoHash; }
    public LocalDateTime getExpiraEm() { return expiraEm; }
    public void setExpiraEm(LocalDateTime expiraEm) { this.expiraEm = expiraEm; }
    public Integer getTentativas() { return tentativas; }
    public void setTentativas(Integer tentativas) { this.tentativas = tentativas; }
    public Boolean getVerificado() { return verificado; }
    public void setVerificado(Boolean verificado) { this.verificado = verificado; }
    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
    public LocalDateTime getResetTokenExpiraEm() { return resetTokenExpiraEm; }
    public void setResetTokenExpiraEm(LocalDateTime resetTokenExpiraEm) { this.resetTokenExpiraEm = resetTokenExpiraEm; }
    public Boolean getUsado() { return usado; }
    public void setUsado(Boolean usado) { this.usado = usado; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
