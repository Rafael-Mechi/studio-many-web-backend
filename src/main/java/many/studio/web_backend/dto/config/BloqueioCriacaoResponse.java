package many.studio.web_backend.dto.config;

import java.util.List;

public class BloqueioCriacaoResponse {

    private BloqueioResponse bloqueio;
    private List<AgendamentoAfetadoDto> agendamentosAfetados;

    public BloqueioCriacaoResponse() {
    }

    public BloqueioCriacaoResponse(BloqueioResponse bloqueio, List<AgendamentoAfetadoDto> agendamentosAfetados) {
        this.bloqueio = bloqueio;
        this.agendamentosAfetados = agendamentosAfetados;
    }

    public BloqueioResponse getBloqueio() {
        return bloqueio;
    }

    public void setBloqueio(BloqueioResponse bloqueio) {
        this.bloqueio = bloqueio;
    }

    public List<AgendamentoAfetadoDto> getAgendamentosAfetados() {
        return agendamentosAfetados;
    }

    public void setAgendamentosAfetados(List<AgendamentoAfetadoDto> agendamentosAfetados) {
        this.agendamentosAfetados = agendamentosAfetados;
    }
}
