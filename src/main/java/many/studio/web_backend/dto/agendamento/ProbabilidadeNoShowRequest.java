package many.studio.web_backend.dto.agendamento;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ProbabilidadeNoShowRequest {

    @NotNull
    private Long idAgendamento;

    @NotNull
    private LocalDate dataAgendamento;

    public Long getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Long idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }
}
