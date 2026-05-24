package many.studio.web_backend.dto.agendamento;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class AgendamentoCriacaoRequest {

    @NotNull
    private Long clienteId;

    @NotNull
    private Long profissionalId;

    @NotNull
    private Long pacoteId;

    @NotNull
    private Long usuarioCriadorId;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public Long getPacoteId() {
        return pacoteId;
    }

    public void setPacoteId(Long pacoteId) {
        this.pacoteId = pacoteId;
    }

    public Long getUsuarioCriadorId() {
        return usuarioCriadorId;
    }

    public void setUsuarioCriadorId(Long usuarioCriadorId) {
        this.usuarioCriadorId = usuarioCriadorId;
    }
}