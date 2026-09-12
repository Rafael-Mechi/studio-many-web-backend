package many.studio.web_backend.dto.agendamento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class AgendamentosCriacaoRequest {

    @NotEmpty
    @Valid
    private List<AgendamentoCriacaoRequest> agendamentos;

    public List<AgendamentoCriacaoRequest> getAgendamentos() {
        return agendamentos;
    }

    public void setAgendamentos(List<AgendamentoCriacaoRequest> agendamentos) {
        this.agendamentos = agendamentos;
    }
}
