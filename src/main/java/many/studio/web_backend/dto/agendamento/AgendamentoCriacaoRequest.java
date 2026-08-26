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
    @Future
    private LocalDateTime horario;

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

    public LocalDateTime getHorario() {
        return horario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }
}