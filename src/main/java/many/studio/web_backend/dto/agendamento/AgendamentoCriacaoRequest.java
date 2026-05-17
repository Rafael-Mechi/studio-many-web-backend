package many.studio.web_backend.dto.agendamento;

import jakarta.validation.constraints.*;
import many.studio.web_backend.entity.Agendamento;

import java.time.LocalDateTime;
import java.util.List;

public class AgendamentoCriacaoRequest {

    @NotNull
    private Long clienteId;

    @NotNull
    private Long pacoteId;

    @NotNull
    List<@Future LocalDateTime> horarios;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getPacoteId() {
        return pacoteId;
    }

    public void setPacoteId(Long pacoteId) {
        this.pacoteId = pacoteId;
    }

    public List<LocalDateTime> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<LocalDateTime> horarios) {
        this.horarios = horarios;
    }
}
